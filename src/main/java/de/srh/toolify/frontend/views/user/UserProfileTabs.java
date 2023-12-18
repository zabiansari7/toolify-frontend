package de.srh.toolify.frontend.views.user;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.server.BrowserWindowOpener;

import de.srh.toolify.frontend.client.RestClient;
import de.srh.toolify.frontend.data.EditUser;
import de.srh.toolify.frontend.data.PurchaseHistory;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.data.User;
import de.srh.toolify.frontend.utils.HelperUtil;
import de.srh.toolify.frontend.views.MainLayout;

@PageTitle("Profile | Toolify")
@Route(value = "profile", layout = MainLayout.class)
@Uses(Icon.class)
public class UserProfileTabs extends Composite<VerticalLayout> {

    private static final long serialVersionUID = 1L;

    Binder<User> binder = new Binder<>(User.class);
	VerticalLayout userDetailsMain = new VerticalLayout();
    FormLayout userDetailsFormLayout = new FormLayout();
    TextField firstname = new TextField();
    TextField lastname = new TextField();
    EmailField email = new EmailField();
    TextField mobile = new TextField();
    TextField defaultStreetName = new TextField();
    TextField defaultStreetNumber = new TextField();
    TextField defaultPincode = new TextField();
    TextField defaultCity = new TextField();
    HorizontalLayout userDetailsHorizontalLayout = new HorizontalLayout();
    Button userDetailsEditButton = new Button();
    Button userDetailsSaveButton = new Button();
    Button userDetailsCancelButton = new Button();
    
    String emailFromSession = HelperUtil.getEmailFromSession();
    
	public UserProfileTabs() {
		binder.bindInstanceFields(this);
        HorizontalLayout layoutRow = new HorizontalLayout();
        TabSheet tabSheet = new TabSheet();     
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet, binder);
        getContent().add(layoutRow);
        layoutRow.add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet, Binder<User> binder) {
        tabSheet.add("User Details", new Div(getUserDetailsLayout(binder))).addClassName("tabStyle");
        tabSheet.add("Order History", new Div(getUserOrdersLayout())).addClassName("tabStyle");
        tabSheet.add("Manage Address", new Div(getManageAddressesLayout())).addClassName("tabStyle");
    }
    
    private VerticalLayout getUserDetailsLayout(Binder<User> binder) {    
    	
        
        getContent().setWidth("100%"); 
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        userDetailsMain.setWidth("100%");
        userDetailsMain.setMaxWidth("800px");
        userDetailsMain.setHeight("min-content");
        userDetailsFormLayout.setWidth("100%");
        
        firstname.setLabel("First Name");
        firstname.setRequired(true);
        
        lastname.setLabel("Last Name");
        lastname.setRequired(true);
        
        email.setLabel("Email");
        
        mobile.setLabel("Mobile");
        mobile.setRequired(true);
        mobile.addValueChangeListener(event -> {
            String value = event.getValue();
            boolean isValid = value.matches("^\\+\\d{0,15}$");
            mobile.setInvalid(!isValid);
            if (isValid) {
            	mobile.setHelperText("");
			} else {
				mobile.setHelperText("Mobile number should start with '+' and then only 15 numbers");
				
			}
            
        });
        defaultStreetName.setLabel("Street");
        defaultStreetName.setRequired(true);
        
        defaultStreetNumber.setLabel("Number");
        defaultStreetNumber.setRequired(true);
        defaultStreetNumber.setPattern("\\d{0,3}");
        defaultStreetNumber.setMaxLength(3);
        defaultStreetNumber.setWidth("min-content");
        defaultStreetNumber.setValueChangeMode(ValueChangeMode.EAGER);
        defaultStreetNumber.addValueChangeListener(event -> {
            String newValue = event.getValue().replaceAll(",", "");
            defaultStreetNumber.setValue(newValue);
        });
        
        defaultPincode.setLabel("Pincode");
        defaultPincode.setValueChangeMode(ValueChangeMode.EAGER);
        defaultPincode.addValueChangeListener(event -> {
            String newValue = event.getValue().replaceAll(",", "");
            defaultPincode.setValue(newValue);
        });
        defaultPincode.setPattern("\\d{0,5}");
        defaultPincode.setWidth("min-content");
        defaultPincode.setMaxLength(5);
        defaultPincode.setRequired(true);
      
        defaultCity.setLabel("City");
        defaultCity.setWidth("min-content");
        defaultCity.setRequired(true);
        
        userDetailsHorizontalLayout.addClassName(Gap.MEDIUM);
        userDetailsHorizontalLayout.setWidth("100%");
        userDetailsHorizontalLayout.getStyle().set("flex-grow", "1");
        userDetailsEditButton.setText("Edit");
        userDetailsEditButton.setWidth("min-content");
        userDetailsEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        userDetailsSaveButton.setText("Save");
        userDetailsSaveButton.setWidth("min-content");
        userDetailsSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        userDetailsCancelButton.setText("Cancel");
        userDetailsCancelButton.setWidth("min-content");
        getContent().add(userDetailsMain);
        userDetailsMain.add(userDetailsFormLayout);
        userDetailsFormLayout.add(firstname);
        userDetailsFormLayout.add(lastname);
        userDetailsFormLayout.add(email);
        userDetailsFormLayout.add(mobile);
        userDetailsFormLayout.add(defaultStreetName);
        userDetailsFormLayout.add(defaultStreetNumber);
        userDetailsFormLayout.add(defaultPincode);
        userDetailsFormLayout.add(defaultCity);
        userDetailsMain.add(userDetailsHorizontalLayout);
        userDetailsHorizontalLayout.add(userDetailsEditButton);
        //userDetailsHorizontalLayout.add(userDetailsCancelButton);  
        
        User user = getUserByEmail();
        
        binder.setBean(user);
        binder.setReadOnly(true);

        userDetailsEditButton.addClickListener(e -> {
        	binder.setReadOnly(false);
        	email.setReadOnly(true);
        	binder.getFields().forEach(field -> field.setRequiredIndicatorVisible(true));
        	email.setRequiredIndicatorVisible(false);
        	userDetailsHorizontalLayout.removeAll();
        	userDetailsHorizontalLayout.add(userDetailsSaveButton, userDetailsCancelButton);
        });
        
        userDetailsCancelButton.addClickListener(e -> {
        	binder.setReadOnly(true);
        	userDetailsHorizontalLayout.removeAll();
        	userDetailsHorizontalLayout.add(userDetailsEditButton);
        	UI.getCurrent().getPage().reload();
        });
        
        userDetailsSaveButton.addClickListener(e -> {
        	if (binder.getFields().anyMatch(a -> a.isEmpty())) {
    			showNotification("Empty fields detected !", NotificationVariant.LUMO_ERROR);
    			return;
    		}        	
        	String encodedEmail = null;
            try {
    			encodedEmail = URLEncoder.encode(emailFromSession, StandardCharsets.UTF_8.toString());
    		} catch (UnsupportedEncodingException ex) {
    			ex.printStackTrace();
    		}
            
        	EditUser editUser = prepareEditUser();
        	RestClient client = new RestClient();
        	client.requestHttp("PUT", "http://localhost:8080/private/user?email=" + encodedEmail, editUser, EditUser.class);
        	System.out.println("CHECK :: " + binder.getBean().getEmail());
        	userDetailsHorizontalLayout.removeAll();
        	userDetailsHorizontalLayout.add(userDetailsEditButton);
        	binder.setReadOnly(true);
        	showNotification("Your details has been updated successfully", NotificationVariant.LUMO_SUCCESS);
        	
        });
        
        return userDetailsMain;
	}
    
    private VerticalLayout getUserOrdersLayout() {    	
    	VerticalLayout main = new VerticalLayout();
    	
    	HorizontalLayout labelHorizontalLayout = headerLayout();
    	main.add(labelHorizontalLayout);
    	
    	JsonNode orderListJsonNode = prepareOrderContent();
    	
    	List<PurchaseHistory> purchaseHistories = HelperUtil.sortByTimeDescending(orderListJsonNode);

    	int count = 0;    	
    	for (PurchaseHistory purchaseHistory : purchaseHistories) {
    		count++;
			HorizontalLayout itemHorizontalLayout = createHorizontalLayout();
			itemHorizontalLayout.add(createLabel(String.valueOf(count)), 
					createLabel(String.valueOf(purchaseHistory.getInvoice())), 
					createLabel(String.valueOf(purchaseHistory.getDate())), 
					createLabel(String.valueOf("€" + purchaseHistory.getTotalPrice())),
					createButton(purchaseHistory.getInvoice()));
			main.add(itemHorizontalLayout);
		}
    	
        return main;
    }
    
    private VerticalLayout getManageAddressesLayout() {
    	VerticalLayout main = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        H4 h4 = new H4();
        H4 h42 = new H4();
        H4 h43 = new H4();
        H4 h44 = new H4();
        H4 h45 = new H4();
        Button buttonPrimary = new Button();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        H5 h5 = new H5();
        H5 h52 = new H5();
        H5 h53 = new H5();
        H5 h54 = new H5();
        H5 h55 = new H5();
        Button buttonSecondary = new Button();
        main.setWidth("100%");
        main.setHeight("752px");
        layoutRow.setWidthFull();
        main.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("50px");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.START);
        h4.setText("Sr.No");
        h4.setWidth("98px");
        h4.setHeight("29px");
        h42.setText("Street Name");
        h42.setWidth("239px");
        h42.setHeight("29px");
        h43.setText("Street Number");
        h43.setWidth("157px");
        h43.setHeight("29px");
        h44.setText("Postcode");
        h44.setWidth("166px");
        h44.setHeight("29px");
        h45.setText("City");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, h45);
        h45.setWidth("327px");
        h45.setHeight("29px");
        buttonPrimary.setText("Add Address");
        buttonPrimary.getStyle().set("flex-grow", "1");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow2.setWidthFull();
        main.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("55px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.START);
        h5.setText("1");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h5);
        h5.setWidth("98px");
        h5.setHeight("50px");
        h52.setText("behram");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h52);
        h52.setWidth("239px");
        h52.setHeight("50px");
        h53.setText("18");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h53);
        h53.setWidth("157px");
        h53.setHeight("50px");
        h54.setText("400051");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, h54);
        h54.setWidth("166px");
        h54.setHeight("50px");
        h55.setText("Mumbai");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, h55);
        h55.setWidth("327px");
        h55.setHeight("55px");
        buttonSecondary.setText("Delete Address");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, buttonSecondary);
        buttonSecondary.setWidth("min-content");
        buttonSecondary.setHeight("20px");
        main.add(layoutRow);
        layoutRow.add(h4);
        layoutRow.add(h42);
        layoutRow.add(h43);
        layoutRow.add(h44);
        layoutRow.add(h45);
        layoutRow.add(buttonPrimary);
        main.add(layoutRow2);
        layoutRow2.add(h5);
        layoutRow2.add(h52);
        layoutRow2.add(h53);
        layoutRow2.add(h54);
        layoutRow2.add(h55);
        layoutRow2.add(buttonSecondary);
        return main;
	}
    
    private User getUserByEmail() {
    	RestClient client = new RestClient();
    	String encodedEmail = null;
        try {
			encodedEmail = URLEncoder.encode(emailFromSession, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/user?email=" + encodedEmail, null, null);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data.getNode(), User.class);		
	}
    
    private EditUser prepareEditUser() {
    	EditUser editUser = new EditUser();
    	editUser.setFirstname(firstname.getValue());
    	editUser.setLastname(lastname.getValue());
    	editUser.setMobile(mobile.getValue());
    	editUser.setDefaultStreetName(defaultStreetName.getValue());
    	editUser.setDefaultStreetNumber(Long.valueOf(defaultStreetNumber.getValue()));
    	editUser.setDefaultPincode(Long.valueOf(defaultPincode.getValue()));
    	editUser.setDefaultCity(defaultCity.getValue());	
    	return editUser;
	}
    
    private H3 createHeader(String text) {
    	H3 header = new H3(text);
		header.setWidthFull();
		return header;
	}

	private H4 createLabel(String text) {
		H4 label = new H4(text);
		label.setWidth("20%");
		label.setWidthFull();
		addClassName(Padding.Left.MEDIUM);
		return label;
	}
	
	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidthFull();
		horizontalLayout.setHeight("25px");
		return horizontalLayout;
	}
	
	private HorizontalLayout headerLayout() {
		HorizontalLayout h = createHorizontalLayout();
		h.add(createHeader("SrNo."), createHeader("Invoice Number"),createHeader("Date"), createHeader("Total Price"), createHeader("Invoice PDF"));
		return h;
	}
	
	private Button createButton(int invoiceNo) {
		Button button = new Button("Downlaod PDF");
		button.getElement().setProperty("invoiceNo", invoiceNo);
		button.setWidth("20%");
		button.addClassName("clickable-button");
		return button;
	}
	
	private void showNotification(String text, NotificationVariant variant) {
		Notification notification = Notification
				.show(text);
		notification.setDuration(5000);
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.addThemeVariants(variant);
	}
	
	private JsonNode prepareOrderContent() {
		String encodedEmail = null;
    	try {
			encodedEmail = URLEncoder.encode(emailFromSession, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    	RestClient client = new RestClient();
    	ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/purchases/history?email=" + encodedEmail, null, null);
    	
    	JsonNode purchaseOrderNode = data.getNode();
    	return purchaseOrderNode;
	}
	
	private void showPdfInBrowser(byte[] pdfBytes) {
        StreamResource resource = new StreamResource("invoice.pdf", (InputStreamFactory) () -> new ByteArrayInputStream(pdfBytes));
        resource.setContentType("application/pdf");
        
        // now check link in the bookmark
    }
}
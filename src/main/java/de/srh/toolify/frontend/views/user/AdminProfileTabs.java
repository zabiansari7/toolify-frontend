package de.srh.toolify.frontend.views.user;

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
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import de.srh.toolify.frontend.client.RestClient;
import de.srh.toolify.frontend.data.EditUser;
import de.srh.toolify.frontend.data.PurchaseHistory;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.data.User;
import de.srh.toolify.frontend.utils.HelperUtil;
import de.srh.toolify.frontend.views.MainLayout;

@PageTitle("AdminProfile | Toolify")
@Route(value = "adminprofiletabs", layout = MainLayout.class)
@Uses(Icon.class)
public class AdminProfileTabs extends Composite<VerticalLayout> {

    private static final long serialVersionUID = 1L;

    Binder<User> binder = new Binder<>(User.class);
	VerticalLayout adminDetailsMain = new VerticalLayout();
    FormLayout adminDetailsFormLayout = new FormLayout();
    TextField firstname = new TextField();
    TextField lastname = new TextField();
    EmailField email = new EmailField();
    TextField mobile = new TextField();
    TextField defaultStreetName = new TextField();
    TextField defaultStreetNumber = new TextField();
    TextField defaultPincode = new TextField();
    TextField defaultCity = new TextField();
    HorizontalLayout adminDetailsHorizontalLayout = new HorizontalLayout();
    Button adminDetailsEditButton = new Button();
    Button adminDetailsSaveButton = new Button();
    Button adminDetailsCancelButton = new Button();
    
    String emailFromSession = HelperUtil.getEmailFromSession();
    
	public AdminProfileTabs() {
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
        tabSheet.add("Admin Details", new Div(getAdminDetailsLayout(binder))).addClassName("tabStyle");
        tabSheet.add("Users Order History", new Div(getAdminOrdersLayout())).addClassName("tabStyle");
        tabSheet.add("Manage Products", new Div(getManageProductsLayout())).addClassName("tabStyle");
    }
    
private VerticalLayout getAdminDetailsLayout(Binder<User> binder) {    
    	
        
        getContent().setWidth("100%"); 
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        adminDetailsMain.setWidth("100%");
        adminDetailsMain.setMaxWidth("800px");
        adminDetailsMain.setHeight("min-content");
        adminDetailsFormLayout.setWidth("100%");
        
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
        
        adminDetailsHorizontalLayout.addClassName(Gap.MEDIUM);
        adminDetailsHorizontalLayout.setWidth("100%");
        adminDetailsHorizontalLayout.getStyle().set("flex-grow", "1");
        adminDetailsEditButton.setText("Edit");
        adminDetailsEditButton.setWidth("min-content");
        adminDetailsEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        adminDetailsSaveButton.setText("Save");
        adminDetailsSaveButton.setWidth("min-content");
        adminDetailsSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        adminDetailsCancelButton.setText("Cancel");
        adminDetailsCancelButton.setWidth("min-content");
        getContent().add(adminDetailsMain);
        adminDetailsMain.add(adminDetailsFormLayout);
        adminDetailsFormLayout.add(firstname);
        adminDetailsFormLayout.add(lastname);
        adminDetailsFormLayout.add(email);
        adminDetailsFormLayout.add(mobile);
        adminDetailsFormLayout.add(defaultStreetName);
        adminDetailsFormLayout.add(defaultStreetNumber);
        adminDetailsFormLayout.add(defaultPincode);
        adminDetailsFormLayout.add(defaultCity);
        adminDetailsMain.add(adminDetailsHorizontalLayout);
        adminDetailsHorizontalLayout.add(adminDetailsEditButton);
        //adminDetailsHorizontalLayout.add(adminDetailsCancelButton);  
        
        
        User admin = getUserByEmail();
        binder.setBean(admin);
        binder.setReadOnly(true);

        adminDetailsEditButton.addClickListener(e -> {
        	binder.setReadOnly(false);
        	email.setReadOnly(true);
        	binder.getFields().forEach(field -> field.setRequiredIndicatorVisible(true));
        	email.setRequiredIndicatorVisible(false);
        	adminDetailsHorizontalLayout.removeAll();
        	adminDetailsHorizontalLayout.add(adminDetailsSaveButton, adminDetailsCancelButton);
        });
        
        adminDetailsCancelButton.addClickListener(e -> {
        	binder.setReadOnly(true);
        	adminDetailsHorizontalLayout.removeAll();
        	adminDetailsHorizontalLayout.add(adminDetailsEditButton);
        	UI.getCurrent().getPage().reload();
        });
        
        adminDetailsSaveButton.addClickListener(e -> {
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
        	adminDetailsHorizontalLayout.removeAll();
        	adminDetailsHorizontalLayout.add(adminDetailsEditButton);
        	binder.setReadOnly(true);
        	showNotification("Your details has been updated successfully", NotificationVariant.LUMO_SUCCESS);
        	
        });
        
        return adminDetailsMain;
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


	private void showNotification(String text, NotificationVariant variant) {
		Notification notification = Notification
				.show(text);
		notification.setDuration(5000);
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.addThemeVariants(variant);
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




	private VerticalLayout getAdminOrdersLayout() {    	
		VerticalLayout main = new VerticalLayout();
		
		HorizontalLayout labelHorizontalLayout = headerLayout();
		main.add(labelHorizontalLayout);
		
		JsonNode orderListJsonNode = getPurchaseHistoryAll();
		
		List<PurchaseHistory> purchaseHistories = HelperUtil.sortByTimeDescending(orderListJsonNode);
	
		int count = 0;    	
		for (PurchaseHistory purchaseHistory : purchaseHistories) {
			count++;
			HorizontalLayout itemHorizontalLayout = createHorizontalLayout();
			itemHorizontalLayout.add(createLabel(String.valueOf(count)),
					createLabel(purchaseHistory.getUser().getEmail()),
					createLabel(String.valueOf(purchaseHistory.getDate())),
					createLabel(String.valueOf(purchaseHistory.getInvoice())),
					createButton(purchaseHistory.getInvoice()));
			main.add(itemHorizontalLayout);
		}
		
	    return main;
	}
	
	private HorizontalLayout headerLayout() {
		HorizontalLayout h = createHorizontalLayout();
		h.add(createHeader("SrNo."), createHeader("User Email"),createHeader("Purchase Date"), createHeader("Invoice Number"), createHeader("View Invoice"));
		return h;
	}
	
	private HorizontalLayout createHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidthFull();
		horizontalLayout.setHeight("25px");
		return horizontalLayout;
	}
	
	private H4 createLabel(String text) {
		H4 label = new H4(text);
		label.setWidth("20%");
		label.setWidthFull();
		addClassName(Padding.Left.MEDIUM);
		return label;
	}
	
	private Button createButton(int invoiceNo) {
		Button button = new Button("Downlaod PDF");
		button.getElement().setProperty("invoiceNo", invoiceNo);
		button.setWidth("20%");
		button.addClassName("clickable-button");
		return button;
	}
	
	private H3 createHeader(String text) {
    	H3 header = new H3(text);
		header.setWidthFull();
		return header;
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
	
	private VerticalLayout getManageProductsLayout() {
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
        h42.setText("Product Name");
        h42.setWidth("239px");
        h42.setHeight("29px");
        h43.setText("Quantity");
        h43.setWidth("157px");
        h43.setHeight("29px");
        h44.setText("Price");
        h44.setWidth("166px");
        h44.setHeight("29px");
        h45.setText("City");
        
        buttonPrimary.setText("Add Product");
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
        h52.setText("Cordless Hammer Drill");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h52);
        h52.setWidth("239px");
        h52.setHeight("50px");
        h53.setText("15");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h53);
        h53.setWidth("157px");
        h53.setHeight("50px");
        h54.setText("€ 259.99");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, h55);
        h55.setWidth("327px");
        h55.setHeight("55px");
        buttonSecondary.setText("Edit");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, buttonSecondary);
        buttonSecondary.setWidth("min-content");
        buttonSecondary.setHeight("20px");
        buttonSecondary.setText("Delete");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, buttonSecondary);
        buttonSecondary.setWidth("min-content");
        buttonSecondary.setHeight("20px");
        main.add(layoutRow);
        layoutRow.add(h4);
        layoutRow.add(h42);
        layoutRow.add(h43);
        layoutRow.add(h44);
        layoutRow.add(buttonPrimary);
        main.add(layoutRow2);
        layoutRow2.add(h5);
        layoutRow2.add(h52);
        layoutRow2.add(h53);
        layoutRow2.add(h54);
        layoutRow2.add(buttonSecondary);
        return main;
	}
	
	private JsonNode getPurchaseHistoryAll() {
		RestClient client = new RestClient();
		ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/admin/purchases/history/all", null, null);
		JsonNode purchaseHistoryNode = data.getNode();
		return purchaseHistoryNode;
	}
}
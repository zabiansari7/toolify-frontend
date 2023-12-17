package de.srh.toolify.frontend.views.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import de.srh.toolify.frontend.client.RestClient;
import de.srh.toolify.frontend.data.EditUser;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.data.User;
import de.srh.toolify.frontend.utils.HelperUtil;
import de.srh.toolify.frontend.views.MainLayout;

@PageTitle("Profile | Toolify")
@Route(value = "profile", layout = MainLayout.class)
@Uses(Icon.class)
public class UserProfileTabs extends Composite<VerticalLayout> {

    private static final long serialVersionUID = 1L;

    Binder<EditUser> binder = new Binder<>(EditUser.class);
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
        setTabSheetSampleData(tabSheet);
        getContent().add(layoutRow);
        layoutRow.add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        tabSheet.add("User Details", new Div(getUserDetailsLayout()));
        tabSheet.add("Order History", new Div(getUserOrdersLayout()));
        tabSheet.add("Manage Address", new Div(getManageAddressesLayout()));
    }
    
    private VerticalLayout getUserDetailsLayout() {    
    	
        
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        userDetailsMain.setWidth("100%");
        userDetailsMain.setMaxWidth("800px");
        userDetailsMain.setHeight("min-content");
        userDetailsFormLayout.setWidth("100%");
        firstname.setLabel("First Name");
        lastname.setLabel("Last Name");
        email.setLabel("Email");
        mobile.setLabel("Mobile");
        defaultStreetName.setLabel("Street");
        defaultStreetNumber.setLabel("Number");
        defaultStreetNumber.setWidth("min-content");
        defaultPincode.setLabel("Pincode");
        defaultPincode.setWidth("min-content");
        defaultCity.setLabel("City");
        defaultCity.setWidth("min-content");
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
        
        EditUser user = getUserByEmail();
        
        binder.setBean(user);
        binder.setReadOnly(true);
        
        userDetailsEditButton.addClickListener(e -> {
        	binder.setReadOnly(false);
        	email.setReadOnly(true);
        	userDetailsHorizontalLayout.removeAll();
        	userDetailsHorizontalLayout.add(userDetailsSaveButton, userDetailsCancelButton);
        });
        
        userDetailsCancelButton.addClickListener(e -> {
        	binder.setReadOnly(true);
        	userDetailsHorizontalLayout.removeAll();
        	userDetailsHorizontalLayout.add(userDetailsEditButton);
        });
        
        userDetailsSaveButton.addClickListener(e -> {
        	EditUser editedUser = binder.getBean();
        	RestClient client = new RestClient();
        	client.requestHttp("PUT", "http://localhost:8080/private/user?email=", editedUser, EditUser.class);
        });
        
        return userDetailsMain;
	}
    
    private VerticalLayout getUserOrdersLayout() {    	
    	VerticalLayout main = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        H4 h4 = new H4();
        H4 h42 = new H4();
        H4 h43 = new H4();
        H4 h44 = new H4();
        H4 h45 = new H4();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        H5 h5 = new H5();
        H5 h52 = new H5();
        H5 h53 = new H5();
        H5 h54 = new H5();
        Button buttonPrimary = new Button();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        main.setWidth("100%");
        main.getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        main.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("30px");
        layoutRow.setAlignItems(Alignment.START);
        layoutRow.setJustifyContentMode(JustifyContentMode.START);
        h4.setText("Sr.No");
        layoutRow.setAlignSelf(FlexComponent.Alignment.START, h4);
        h4.setWidth("67px");
        h42.setText("Invoice number");
        h42.getStyle().set("flex-grow", "1");
        h43.setText("Date");
        h43.setWidth("192px");
        h44.setText("Total Price");
        h44.getStyle().set("flex-grow", "1");
        h45.setText("Invoice");
        h45.setWidth("290px");
        layoutRow2.setWidthFull();
        main.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("56px");
        h5.setText("1");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h5);
        h5.setWidth("67px");
        h52.setText("12345");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h52);
        h52.setWidth("363px");
        h53.setText("23.11.2023");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h53);
        h53.setWidth("192px");
        h54.setText("€777");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, h54);
        h54.setWidth("320px");
        buttonPrimary.setText("Download PDF");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, buttonPrimary);
        buttonPrimary.setWidth("290px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow3.setWidthFull();
        main.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        main.add(layoutRow);
        layoutRow.add(h4);
        layoutRow.add(h42);
        layoutRow.add(h43);
        layoutRow.add(h44);
        layoutRow.add(h45);
        main.add(layoutRow2);
        layoutRow2.add(h5);
        layoutRow2.add(h52);
        layoutRow2.add(h53);
        layoutRow2.add(h54);
        layoutRow2.add(buttonPrimary);
        main.add(layoutRow3);
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
    
    private EditUser getUserByEmail() {
    	String email = HelperUtil.getEmailFromSession();
    	RestClient client = new RestClient();
    	String encodedEmail = null;
        try {
			encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/user?email=" + encodedEmail, null, null);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data.getNode(), EditUser.class);		
	}
}
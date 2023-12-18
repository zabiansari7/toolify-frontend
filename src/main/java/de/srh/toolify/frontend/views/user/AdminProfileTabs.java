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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.tabs.Tab;
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
import de.srh.toolify.frontend.data.Address;
import de.srh.toolify.frontend.data.Category;
import de.srh.toolify.frontend.data.EditUser;
import de.srh.toolify.frontend.data.Product;
import de.srh.toolify.frontend.data.PurchaseHistory;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.data.User;
import de.srh.toolify.frontend.utils.HelperUtil;
import de.srh.toolify.frontend.views.MainLayout;

@PageTitle("AdminProfile | Toolify")
@Route(value = "admin", layout = MainLayout.class)
@Uses(Icon.class)
public class AdminProfileTabs extends Composite<VerticalLayout> {

    private static final long serialVersionUID = 1L;

    Binder<User> binder = new Binder<>(User.class);
    Binder<Product> binderProduct = new Binder<>(Product.class);
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
    HorizontalLayout layoutRow = new HorizontalLayout();
    
	HorizontalLayout layoutRowDialog = new HorizontalLayout();
    TextField name = new TextField();
    TextField description = new TextField();
    TextField price = new TextField();
    HorizontalLayout layoutRow2 = new HorizontalLayout();
    TextField quantity = new TextField();
    TextField voltage = new TextField();
    TextField productDimensions = new TextField();
    HorizontalLayout layoutRow3 = new HorizontalLayout();
    TextField itemWeight = new TextField();
    TextField bodyMaterial = new TextField();
    TextField itemModelNumber = new TextField();
    HorizontalLayout layoutRow4 = new HorizontalLayout();
    TextField design = new TextField();
    TextField colour = new TextField();
    TextField batteriesRequired = new TextField();
    HorizontalLayout layoutRow5 = new HorizontalLayout();
    TextField image = new TextField();
    Select<Category> category = new Select<>();
    HorizontalLayout layoutRow6 = new HorizontalLayout();
    Button saveButton = new Button();
    Button cancelButton = new Button();
    
    String emailFromSession = HelperUtil.getEmailFromSession();
    Category categorySelectValue;
    
    private boolean valuesMatches;
    
	public AdminProfileTabs() {
		binder.bindInstanceFields(this);
		binderProduct.bindInstanceFields(this);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet, binder);
        getContent().add(layoutRow);
        layoutRow.add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet, Binder<User> binder) {
        tabSheet.add("Admin Details", new Div(getAdminDetailsLayout(binder))).addClassName("tabStyle");
        tabSheet.add("Users Order History", new Div(getAdminOrdersLayout())).addClassName("tabStyle");
        tabSheet.add("Manage Products", new Div(getManageProductsLayout())).addClassName("tabStyle");
        tabSheet.add("Manage Categoty", new Div(getManageProductsLayout())).addClassName("tabStyle");
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
        firstname.setValueChangeMode(ValueChangeMode.EAGER);
        firstname.setRequired(true);
        firstname.setPattern("^[a-zA-Z]*$");
        firstname.setMaxLength(30);
        firstname.setRequiredIndicatorVisible(true);
        firstname.setClearButtonVisible(true);
        firstname.addValueChangeListener(event -> {
        	String value = event.getValue();
        	boolean isValid = value.matches(("\"^[a-zA-Z]*$\""));
        	firstname.setInvalid(!isValid);	
        });
        
        lastname.setLabel("Last Name");
        lastname.setValueChangeMode(ValueChangeMode.EAGER);
        lastname.setMaxLength(30);
        lastname.setRequiredIndicatorVisible(true);
        lastname.setRequired(true);
        lastname.setPattern("^[a-zA-Z]*$");
        lastname.addValueChangeListener(event -> {
        	String value = event.getValue();
        	boolean isValid = value.matches(("^[a-zA-Z]*$"));
        	lastname.setInvalid(!isValid);
        });
        
        email.setLabel("Email");
        
        mobile.setLabel("Mobile");
        mobile.setPattern("^\\+\\d{0,15}$");
        mobile.setRequired(true);
        mobile.setMaxLength(15);
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
        defaultStreetName.setValueChangeMode(ValueChangeMode.EAGER);
        defaultStreetName.setRequiredIndicatorVisible(true);
        defaultStreetName.setPattern("^[a-zA-Z]*$");
        defaultStreetName.setRequired(true);
        defaultStreetName.setMaxLength(30);
        defaultStreetName.addValueChangeListener(event -> {
        	String value = event.getValue();
        	boolean isValid = value.matches(("\"^[a-zA-Z]*$\""));
        	defaultStreetName.setInvalid(!isValid);
        });
        
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
        defaultCity.setValueChangeMode(ValueChangeMode.EAGER);
        defaultCity.setPattern("^[a-zA-Z ]*$");;
        defaultCity.setMaxLength(30);
        defaultCity.setRequiredIndicatorVisible(true);
        defaultCity.addValueChangeListener(event -> {
        	String value = event.getValue();
        	boolean isValid = value.matches(("^[a-zA-Z ]*$"));
        	defaultCity.setInvalid(!isValid);
        });
        
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
        	if (binder.validate().isOk() == false) {
        		showNotification("Please correct your input", NotificationVariant.LUMO_ERROR);
    			return;
			}
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
		
		HorizontalLayout labelHorizontalLayout = headerOrderHistoryLayout();
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
	
	private HorizontalLayout headerOrderHistoryLayout() {
		HorizontalLayout h = createHorizontalLayout();
		h.add(createHeader("SrNo."), createHeader("User Email"),createHeader("Purchase Date"), createHeader("Invoice Number"), createHeader("View Invoice"));
		return h;
	}

	private HorizontalLayout headerManageProductsLayout(VerticalLayout mainLayout) {
		HorizontalLayout h = createHorizontalLayout();
		h.add(createHeader("SrNo."), createHeader("Product Name"),createHeader("Quantity"), createHeader("Price"), createAddProductButton(mainLayout));
		return h;
	}
	
	private Button createAddProductButton(VerticalLayout mainLayout) {
		Button button = new Button("Add Product");
		button.setWidth("20%");
		button.addClassName("clickable-button");
		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		button.addClickListener(event -> {
			Dialog dialog = new Dialog();
			dialog.getElement().setAttribute("aria-label", "Create new product");
			mainLayout.add(dialog);
			dialog.add(createAddProductDialog(dialog, mainLayout));
			dialog.open();
		});
		return button;
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
	
	private HorizontalLayout createEditDeleteLayout() {
		HorizontalLayout h = createHorizontalLayout();
		h.add(createEditButton(), createDeleteButton());
		return h;
	}
	
	private Button createEditButton() {
		Button button = new Button("Edit");
		button.setWidth("50%");
		button.addClassName("clickable-button");
		return button;
	}
	
	private Button createDeleteButton() {
		Button button = new Button("Delete");
		button.setWidth("50%");
		button.addClassName("clickable-button");
		button.addThemeVariants(ButtonVariant.LUMO_ERROR);
		return button;
	}
	
	private VerticalLayout getManageProductsLayout() {
    	VerticalLayout main = new VerticalLayout();
    	
    	HorizontalLayout labeHorizontalLayout = headerManageProductsLayout(main);
    	main.add(labeHorizontalLayout);
        main.setWidth("100%");
        
        JsonNode productsNode = getAllProducts();
        ObjectMapper mapper = HelperUtil.getObjectMapper();
    	int count = 0;
    	for (JsonNode productNode : productsNode) {
    		count++;
    		Product product = mapper.convertValue(productNode, Product.class);
			
			HorizontalLayout addressHorizontalLayout = createHorizontalLayout();
			addressHorizontalLayout.add(
					createLabel(String.valueOf(count)), 
					createLabel(product.getName()), 
					createLabel(String.valueOf(product.getQuantity())), 
					createLabel(String.valueOf(product.getPrice())), 
					createEditDeleteLayout());
					main.add(addressHorizontalLayout);
    	}
        return main;
	}
	
	private JsonNode getPurchaseHistoryAll() {
		RestClient client = new RestClient();
		ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/admin/purchases/history/all", null, null);
		JsonNode purchaseHistoryNode = data.getNode();
		return purchaseHistoryNode;
	}
	
	private VerticalLayout createAddProductDialog(Dialog dialog, VerticalLayout main) {
		VerticalLayout dialogVerticalLayout = new VerticalLayout();
		
        dialogVerticalLayout.setWidth("100%");
        dialogVerticalLayout.getStyle().set("flex-grow", "1");
        layoutRowDialog.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRowDialog);
        layoutRowDialog.addClassName(Gap.MEDIUM);
        //layoutRow.setWidth("1100px");
        layoutRowDialog.setHeight("75px");
        layoutRowDialog.setAlignItems(Alignment.CENTER);
        layoutRowDialog.setJustifyContentMode(JustifyContentMode.CENTER);
        name.setLabel("Name");
        //name.setWidth("180px");
        description.setLabel("Description");
        //description.setWidth("180px");
        price.setLabel("Price");
        //price.setWidth("180px");
        layoutRow2.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidthFull();
        layoutRow2.setHeight("75px");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);
        quantity.setLabel("Quantity");
        //quantity.setWidth("180px");
        voltage.setLabel("Voltage");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, voltage);
        //voltage.setWidth("180px");
        productDimensions.setLabel("Product Dimensions");
        //productDimensions.setWidth("180px");
        layoutRow3.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        //layoutRow3.setWidth("1100px");
        layoutRow3.setHeight("75px");
        layoutRow3.setAlignItems(Alignment.CENTER);
        layoutRow3.setJustifyContentMode(JustifyContentMode.CENTER);
        itemWeight.setLabel("Item Weight");
        //itemWeight.setWidth("180px");
        bodyMaterial.setLabel("Body Material");
        //bodyMaterial.setWidth("180px");
        itemModelNumber.setLabel("Item Model Number");
        //itemModelNumber.setWidth("180px");
        layoutRow4.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        //layoutRow4.setWidth("1100px");
        layoutRow4.setHeight("75px");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.CENTER);
        design.setLabel("Design");
        //design.setWidth("180px");
        colour.setLabel("Colour");
        //colour.setWidth("180px");
        batteriesRequired.setLabel("Batteries Required");
        //batteriesRequired.setWidth("180px");
        layoutRow5.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        //layoutRow5.setWidth("1100px");
        layoutRow5.setHeight("75px");
        layoutRow5.setAlignItems(Alignment.CENTER);
        layoutRow5.setJustifyContentMode(JustifyContentMode.CENTER);
        image.setLabel("Image URL");
        //image.setWidth("180px");
        category.setLabel("Category");
        category.setPlaceholder("Select Category");
        List<Category> categories = HelperUtil.getAllCategoriesAsClass();
        category.setItems(categories);
        category.addValueChangeListener(e -> {
        	this.setCategorySelectValue(e.getValue());
        });
        
        //category.setWidth("180px");
        //setComboBoxSampleData(category);
        layoutRow6.setWidthFull();
        dialogVerticalLayout.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        //layoutRow6.setWidth("1100px");
        layoutRow6.getStyle().set("flex-grow", "1");
        layoutRow6.setAlignItems(Alignment.CENTER);
        layoutRow6.setJustifyContentMode(JustifyContentMode.CENTER);
        saveButton.setText("Save");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.setText("Cancel");
        cancelButton.setWidth("min-content");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(e -> {
        	dialog.close();
        });
        dialogVerticalLayout.add(layoutRowDialog);
        layoutRowDialog.add(name);
        layoutRowDialog.add(description);
        layoutRowDialog.add(price);
        dialogVerticalLayout.add(layoutRow2);
        layoutRow2.add(quantity);
        layoutRow2.add(voltage);
        layoutRow2.add(productDimensions);
        dialogVerticalLayout.add(layoutRow3);
        layoutRow3.add(itemWeight);
        layoutRow3.add(bodyMaterial);
        layoutRow3.add(itemModelNumber);
        dialogVerticalLayout.add(layoutRow4);
        layoutRow4.add(design);
        layoutRow4.add(colour);
        layoutRow4.add(batteriesRequired);
        dialogVerticalLayout.add(layoutRow5);
        layoutRow5.add(image);
        layoutRow5.add(category);
        dialogVerticalLayout.add(layoutRow6);
        layoutRow6.add(saveButton);
        layoutRow6.add(cancelButton);

        Product product = new Product();
        binderProduct.forField(category)
        	.bind(Product::getCategory, Product::setCategory);
        binderProduct.setBean(product);
        
        saveButton.addClickListener(e -> {
        	addProduct(binderProduct);
        	showNotification("Product saved successfully", NotificationVariant.LUMO_SUCCESS);
        	dialog.close();
        	main.removeAll();
        	main.getElement().executeJs("location.reload(true)");
        });
                
		return dialogVerticalLayout;
	}
	
	private  JsonNode addProduct(Binder<Product> productBinder) {
		
		Product product = productBinder.getBean();
		
		RestClient client = new RestClient();
		ResponseData data = client.requestHttp("POST","http://localhost:8080/private/admin/products/product", product, Product.class);
		JsonNode productResponse = data.getNode();
		return productResponse;
	}
	
	private JsonNode getAllProducts() {
		RestClient client = new RestClient();
		ResponseData data = client.requestHttp("GET", "http://localhost:8080/public/products/all", null, null);
		JsonNode productsNode = data.getNode();
		return productsNode;
	}

	public Category getCategorySelectValue() {
		return categorySelectValue;
	}

	public void setCategorySelectValue(Category categorySelectValue) {
		this.categorySelectValue = categorySelectValue;
	}

	public boolean isValuesMatches() {
		return valuesMatches;
	}

	public void setValuesMatches(boolean valuesMatches) {
		this.valuesMatches = valuesMatches;
	}
	
}
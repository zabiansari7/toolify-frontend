package de.srh.toolify.frontend.views.checkout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import de.srh.toolify.frontend.client.RestClient;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.data.User;
import de.srh.toolify.frontend.views.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Checkout")
@Route(value = "checkout", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class CheckoutView extends Composite<VerticalLayout> {
	
	HorizontalLayout layoutRow = new HorizontalLayout();

    HorizontalLayout layoutRow2 = new HorizontalLayout();
    VerticalLayout layoutColumn2 = new VerticalLayout();
    H2 h2 = new H2();
    H4 h4 = new H4();
    HorizontalLayout layoutRow3 = new HorizontalLayout();
    TextField firstname = new TextField();
    TextField lastname = new TextField();
    H4 h42 = new H4();
    HorizontalLayout layoutRow4 = new HorizontalLayout();
    TextField defaultStreetName = new TextField();
    TextField defaultStreetNumber = new TextField();
    HorizontalLayout layoutRow5 = new HorizontalLayout();
    TextField defaultPincode = new TextField();
    TextField defaultCity = new TextField();
    ComboBox selectAddress = new ComboBox();
    VerticalLayout layoutColumn3 = new VerticalLayout();
    H2 h22 = new H2();
    HorizontalLayout layoutRow6 = new HorizontalLayout();
    H4 h43 = new H4();
    H4 h44 = new H4();
    H4 h45 = new H4();
    HorizontalLayout layoutRow7 = new HorizontalLayout();
    H5 h5 = new H5();
    H5 h52 = new H5();
    H5 h53 = new H5();
    HorizontalLayout layoutRow8 = new HorizontalLayout();
    H3 h3 = new H3();
    Button payButton = new Button();

    public CheckoutView() {
        
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setAlignItems(Alignment.START);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);

        layoutRow2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutColumn2.setHeightFull();
        layoutRow2.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("634px");
        layoutColumn2.setHeight("min-content");
        h2.setText("Checkout");
        h2.setWidth("max-content");
        h4.setText("Personal Details");
        h4.setWidth("max-content");
        layoutRow3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.setHeight("min-content");
        firstname.setLabel("First Name");
        firstname.getStyle().set("flex-grow", "1");
        firstname.setHeight("75px");
        lastname.setLabel("Last Name");
        lastname.getStyle().set("flex-grow", "1");
        lastname.setHeight("75px");
        h42.setText("Shipping Address");
        h42.setWidth("max-content");
        layoutRow4.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("100%");
        layoutRow4.setHeight("min-content");
        defaultStreetName.setLabel("Street Name");
        defaultStreetName.setWidth("400px");
        defaultStreetName.setHeight("50px");
        defaultStreetNumber.setLabel("Street Number");
        defaultStreetNumber.setWidth("400px");
        defaultStreetNumber.setHeight("50px");
        layoutRow5.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.setHeight("min-content");
        defaultPincode.setLabel("Postcode");
        defaultPincode.setWidth("400px");
        defaultPincode.setHeight("50px");
        defaultCity.setLabel("City");
        defaultCity.setWidth("400px");
        defaultCity.setHeight("50px");
        selectAddress.setLabel("Select Address");
        selectAddress.setWidth("100%");
        setComboBoxSampleData(selectAddress);
        layoutColumn3.setHeightFull();
        layoutRow2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("634px");
        layoutColumn3.setHeight("100%");
        h22.setText("Order");
        h22.setWidth("max-content");
        layoutRow6.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("602px");
        layoutRow6.setHeight("25px");
        layoutRow6.setAlignItems(Alignment.START);
        layoutRow6.setJustifyContentMode(JustifyContentMode.START);
        h43.setText("Product");
        h43.setWidth("250px");
        h44.setText("Quantity");
        h44.setWidth("max-content");
        h45.setText("Price");
        h45.setWidth("max-content");
        layoutRow7.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow7);
        layoutRow7.addClassName(Gap.MEDIUM);
        layoutRow7.setWidth("100%");
        layoutRow7.setHeight("25px");
        h5.setText("Cordless Hammer Drill");
        h5.setWidth("250px");
        h52.setText("2");
        h52.setWidth("71px");
        h53.setText("€259.99");
        h53.setWidth("max-content");
        layoutRow8.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow8);
        layoutRow8.addClassName(Gap.MEDIUM);
        layoutRow8.setWidth("100%");
        layoutRow8.setHeight("min-content");
        layoutRow8.setAlignItems(Alignment.CENTER);
        layoutRow8.setJustifyContentMode(JustifyContentMode.END);
        h3.setText("Total Price €259.99");
        h3.setWidth("max-content");
        payButton.setText("Pay");
        payButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        payButton.addClassName("payButton");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.END, payButton);
        payButton.setWidth("200px");
        getContent().add(layoutRow);

        getContent().add(layoutRow2);
        layoutRow2.add(layoutColumn2);
        layoutColumn2.add(h2);
        layoutColumn2.add(h4);
        layoutColumn2.add(layoutRow3);
        layoutRow3.add(firstname);
        layoutRow3.add(lastname);
        layoutColumn2.add(h42);
        layoutColumn2.add(layoutRow4);
        layoutRow4.add(defaultStreetName);
        layoutRow4.add(defaultStreetNumber);
        layoutColumn2.add(layoutRow5);
        layoutRow5.add(defaultPincode);
        layoutRow5.add(defaultCity);
        layoutColumn2.add(selectAddress);
        layoutRow2.add(layoutColumn3);
        layoutColumn3.add(h22);
        layoutColumn3.add(layoutRow6);
        layoutRow6.add(h43);
        layoutRow6.add(h44);
        layoutRow6.add(h45);
        layoutColumn3.add(layoutRow7);
        layoutRow7.add(h5);
        layoutRow7.add(h52);
        layoutRow7.add(h53);
        layoutColumn3.add(layoutRow8);
        layoutRow8.add(h3);
        layoutColumn3.add(payButton);
        
        
        firstname.setEnabled(false);
        lastname.setEnabled(false);
        defaultStreetName.setEnabled(false);
        defaultStreetNumber.setEnabled(false);
        defaultPincode.setEnabled(false);
        defaultCity.setEnabled(false);
        
        RestClient client = new RestClient();
        String email = getEmailFromSession();
        String encodedParam = null;
        try {
			encodedParam = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ResponseData data = client.requestHttp("GET", "http://localhost:8080/private/user?email=" + encodedParam, null, null);
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(data.getNode(), User.class);
        
        firstname.setValue(user.getFirstname());
        lastname.setValue(user.getLastname());
        defaultStreetName.setValue(user.getDefaultStreetName());
        defaultStreetNumber.setValue(String.valueOf(user.getDefaultStreetNumber()));
        defaultPincode.setValue(String.valueOf(user.getDefaultPincode()));
        defaultCity.setValue(user.getDefaultCity());
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }
    
    private String getEmailFromSession() {
    	JsonNode userNode = (JsonNode) VaadinSession.getCurrent().getAttribute("user");
    	return userNode.get("email").toString();
	}
}

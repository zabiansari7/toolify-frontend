package de.srh.toolify.frontend.views.cart;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import de.srh.toolify.frontend.client.RestClient;
import de.srh.toolify.frontend.data.Product;
import de.srh.toolify.frontend.data.PurchaseItem;
import de.srh.toolify.frontend.data.ResponseData;
import de.srh.toolify.frontend.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Toolify | Cart")
@Route(value = "cart", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class CartView extends Composite<VerticalLayout> {

    private static final long serialVersionUID = -8245968777291604244L;
    
    VerticalLayout layoutColumn3 = new VerticalLayout();

    HorizontalLayout layoutRow4 = new HorizontalLayout();
    H3 totalPriceLabel = new H3();
    HorizontalLayout layoutRow5 = new HorizontalLayout();
    Button checkoutButton = new Button();
    Long productId;
    
    
    HorizontalLayout headerLayout = new HorizontalLayout();
    

	public CartView() {        
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn3.setWidthFull();
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");




        layoutRow4.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("100%");
        layoutRow4.getStyle().set("flex-grow", "1");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.END);
        totalPriceLabel.setText("Total Price:  € 758");
        layoutRow4.setAlignSelf(FlexComponent.Alignment.CENTER, totalPriceLabel);
        totalPriceLabel.setWidth("300px");
        totalPriceLabel.setHeight("28px");
        layoutRow5.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.getStyle().set("flex-grow", "1");
        layoutRow5.setAlignItems(Alignment.END);
        layoutRow5.setJustifyContentMode(JustifyContentMode.END);
        checkoutButton.setText("Proceed to Checkout Page");
        checkoutButton.setWidth("300px");
        checkoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        headerLayout.add(createHeader("SrNo."), createHeader("Product"), createHeader("Quantity"), createHeader("Price"));
        headerLayout.setWidthFull();
        headerLayout.addClassName(Gap.MEDIUM);
        
        layoutColumn3.setFlexGrow(1.0, headerLayout);
        
        layoutColumn3.add(layoutRow4);
        layoutRow4.add(totalPriceLabel);
		layoutColumn3.add(layoutRow5);
		layoutRow5.add(checkoutButton);
        
        getContent().add(headerLayout);
     
        
        List<PurchaseItem> carts = CartService.getInstance().getCartItems();
        int count = 1;
        for (PurchaseItem purchaseItem : carts) {
        	HorizontalLayout itemLayout = new HorizontalLayout();
        	itemLayout.setWidthFull();
        	itemLayout.add(createLabel(String.valueOf(count), "sr"+count), createLabel(purchaseItem.getProductName(), "product"+count), createIntegerField(0, purchaseItem.getQuantity(), 1, purchaseItem), createLabel(String.valueOf("€" + purchaseItem.getPurchasePrice()),"price"+ count));
        	count++;
        	   getContent().add(itemLayout);
		}
        getContent().add(layoutColumn3);
    }	
	
	private H3 createHeader(String text) {
		H3 header = new H3(text);
		header.setWidthFull();
        return header;
    }

    private H4 createLabel(String text, String id) {
    	H4 label = new H4(text);
    	label.setWidthFull();
    	label.setId(id);
    	return label;
    }

    private IntegerField createIntegerField(int min, int max, int value, PurchaseItem purchaseItem) {
        IntegerField integerField = new IntegerField();
        integerField.setWidthFull();
        integerField.setStepButtonsVisible(true);
        integerField.setMin(min);
        integerField.setMax(max);
        integerField.setValue(value);
        integerField.addValueChangeListener(e -> {
        	if (e.getValue().equals(0)) {
				integerField.getParent().ifPresent(parent -> parent.getElement().removeFromParent());
				CartService.getInstance().removeFromCart(purchaseItem);
			} else {
				BigDecimal purchasePrice = purchaseItem.getPurchasePrice().multiply(BigDecimal.valueOf(e.getValue()));
				integerField.getElement().getParent().getChild(3).setText(String.valueOf("€" + purchasePrice));
			}
        });
        return integerField;
    }
	
}

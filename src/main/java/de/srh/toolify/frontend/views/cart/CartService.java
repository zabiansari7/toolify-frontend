package de.srh.toolify.frontend.views.cart;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.server.VaadinSession;

import de.srh.toolify.frontend.data.PurchaseItem;

public class CartService {

	private static final String CART_ITEMS_ATTRIBUTE = "cartItems";

    private static final CartService INSTANCE = new CartService();

    private CartService() {}
    
    public static CartService getInstance() {
        return INSTANCE;
    }

    public List<PurchaseItem> getCartItems() {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        List<PurchaseItem> cartItems = (List<PurchaseItem>) vaadinSession.getAttribute(CART_ITEMS_ATTRIBUTE);
        return cartItems != null ? new ArrayList<>(cartItems) : new ArrayList<>();
    }

    public void addToCart(PurchaseItem cartItem) {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        List<PurchaseItem> cartItems = (List<PurchaseItem>) vaadinSession.getAttribute(CART_ITEMS_ATTRIBUTE);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        cartItems.add(cartItem);
        vaadinSession.setAttribute(CART_ITEMS_ATTRIBUTE, cartItems);
    }
    
    public void removeFromCart(PurchaseItem cartItem) {
    	VaadinSession vaadinSession = VaadinSession.getCurrent();
        List<PurchaseItem> cartItems = (List<PurchaseItem>) vaadinSession.getAttribute(CART_ITEMS_ATTRIBUTE);
        cartItems.remove(cartItem);
        vaadinSession.setAttribute(CART_ITEMS_ATTRIBUTE, cartItems);
	}
}

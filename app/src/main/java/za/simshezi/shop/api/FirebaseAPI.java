package za.simshezi.shop.api;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.OrderModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.model.UserModel;

public class FirebaseAPI {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private CollectionReference restaurantsCollection;
    private CollectionReference ordersCollection;
    private CollectionReference customersCollection;
    private static FirebaseAPI firebase;

    private FirebaseAPI() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        restaurantsCollection = db.collection("restaurants");
        customersCollection = db.collection("customers");
        ordersCollection = db.collection("orders");
    }

    public static FirebaseAPI getInstance() {
        if (firebase == null) {
            firebase = new FirebaseAPI();
        }
        return firebase;
    }

    public void addUser(UserModel model, OnSuccessListener<Boolean> callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", model.getEmail());
        user.put("name", model.getName());
        user.put("cellphone", model.getCellphone());
        user.put("credit", model.getCredit());
        user.put("card", model.isCard());

        customersCollection.add(user)
                .addOnSuccessListener(runnable -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onSuccess(false));
    }

    public void editUser(UserModel model, OnSuccessListener<Boolean> callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", model.getEmail());
        user.put("name", model.getName());
        user.put("cellphone", model.getCellphone());

        customersCollection.document(model.getId()).set(user)
                .addOnSuccessListener(runnable -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onSuccess(false));
    }

    public void getCustomer(String email, OnSuccessListener<QuerySnapshot> callback) {
        Query query = customersCollection.whereEqualTo("email", email);
        executeQuery(query, callback);
    }

    public void editAvailableBalance(String id, Double credit, OnSuccessListener<Boolean> callback) {
        customersCollection.document(id).update("credit", credit)
                .addOnSuccessListener((runnable) -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onSuccess(false));
    }
    public void editCardStatus(String id, OnSuccessListener<Boolean> callback) {
        customersCollection.document(id).update("card", true)
                .addOnSuccessListener((runnable) -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onSuccess(false));
    }
    public void setOrder(CartModel cart, OnSuccessListener<Boolean> callback) {
        Map<String, Object> order = new HashMap<>();
        order.put("customer", cart.getUser().getName());
        order.put("email", cart.getUser().getEmail());
        order.put("cellphone", cart.getUser().getCellphone());
        order.put("items", cart.getList().size());
        order.put("payment", cart.getPayment());
        order.put("shopId", cart.getShop().getId());
        order.put("time", Timestamp.now());
        order.put("price", cart.getPrice());
        order.put("status", "Placed");

        ordersCollection.add(order)
                .addOnSuccessListener(documentReference -> {
                    for (ProductModel model : cart.getList()) {
                        Map<String, Object> product = new HashMap<>();
                        product.put("name", model.getName());
                        product.put("price", model.getPrice());
                        documentReference.collection("product").add(product)
                                .addOnSuccessListener(documentRef -> {
                                    for (IngredientModel item : model.getIngredients()) {
                                        Map<String, Object> ingredient = new HashMap<>();
                                        ingredient.put("name", item.getName());
                                        ingredient.put("price", item.getPrice());
                                        ingredient.put("count", item.getCount());
                                        documentRef.collection("ingredient").add(ingredient);
                                    }
                                });
                    }
                    callback.onSuccess(true);
                }).addOnFailureListener(e -> callback.onSuccess(false));
    }

    public void getProducts(String id, OnSuccessListener<QuerySnapshot> callback) {
        Query query = restaurantsCollection.document(id).collection("product");
        executeQuery(query, callback);
    }

    public void getIngredients(String shopId, String productId, OnSuccessListener<QuerySnapshot> callback) {
        Query query = restaurantsCollection.document(shopId).collection("product").document(productId).collection("ingredient");
        executeQuery(query, callback);
    }

    public void getOrders(String email, OnSuccessListener<QuerySnapshot> callback) {
        Query query = ordersCollection.whereEqualTo("email", email);
        executeQuery(query, callback);
    }


    public void getOrderProducts(String orderId, OnSuccessListener<QuerySnapshot> callback) {
        Query query = ordersCollection.document(orderId).collection("product");
        executeQuery(query, callback);
    }

    public void getOrderIngredients(String orderId, String productId, OnSuccessListener<QuerySnapshot> callback) {
        Query query = ordersCollection.document(orderId).collection("product")
                .document(productId).collection("ingredient");
        executeQuery(query, callback);
    }

    public void getShops(OnSuccessListener<QuerySnapshot> callback) {
        Query query = restaurantsCollection.orderBy("rating", Query.Direction.DESCENDING);;
        executeQuery(query, callback);
    }

    private void executeQuery(Query query, OnSuccessListener<QuerySnapshot> callback) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    callback.onSuccess(querySnapshot);
                } else {
                    callback.onSuccess(null); // No matching documents found
                }
            } else {
                callback.onSuccess(null); // Handle the failure
            }
        });
    }

    public void getShopImage(String documentId, OnSuccessListener<byte[]> callback) {
        StorageReference imageRef = storageRef.child("restaurants_images/" + documentId + ".jpg");
        executeImageDownload(callback, imageRef);
    }

    public void getProductImage(String documentId, OnSuccessListener<byte[]> callback) {
        StorageReference imageRef = storageRef.child("products_images/" + documentId + ".jpg");
        executeImageDownload(callback, imageRef);
    }

    private void executeImageDownload(OnSuccessListener<byte[]> callback, StorageReference imageRef) {
        long MAX_DOWNLOAD_SIZE = 1024 * 1024; // 1 MB

        imageRef.getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(callback)
                .addOnFailureListener(exception -> callback.onSuccess(null));
    }

}

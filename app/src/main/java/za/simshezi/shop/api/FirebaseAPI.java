package za.simshezi.shop.api;

import com.google.android.gms.tasks.OnSuccessListener;
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

import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.model.ShopModel;

public class FirebaseAPI {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private CollectionReference restaurantsCollection;
    private CollectionReference ordersCollection;
    private static FirebaseAPI firebase;

    private FirebaseAPI() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        restaurantsCollection = db.collection("restaurants");
        ordersCollection = db.collection("orders");
    }

    public static FirebaseAPI getInstance() {
        if (firebase == null) {
            firebase = new FirebaseAPI();
        }
        return firebase;
    }

    public void getOrders(String cellphone, OnSuccessListener<QuerySnapshot> callback) {
        Query query = ordersCollection.whereEqualTo("cellphone", cellphone);
        executeQuery(query, callback);
    }

    public void getShops(OnSuccessListener<QuerySnapshot> callback) {
        Query query = restaurantsCollection;
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
                .addOnSuccessListener(bytes -> callback.onSuccess(bytes))
                .addOnFailureListener(exception -> callback.onSuccess(null));
    }

    public void setOrder(CartModel cart, OnSuccessListener<Boolean> callback) {
        Map<String, Object> order = new HashMap<>();
        order.put("customer", cart.getCustomer());
        order.put("items", cart.getList().size());
        order.put("payment", cart.getPayment());
        order.put("shopId", cart.getShop().getId());
        order.put("time", Timestamp.now());
        order.put("price", cart.getPrice());

        ordersCollection.add(order)
                .addOnSuccessListener(documentReference -> {
                   callback.onSuccess(true);
                }).addOnFailureListener(e -> {
                    callback.onSuccess(false);
                });
    }
}

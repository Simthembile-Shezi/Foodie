package za.simshezi.shop.api;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FirebaseAPI {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private CollectionReference restaurantsCollection;
    private CollectionReference productsCollection;
    private CollectionReference ingredientsCollection;
    private static FirebaseAPI firebase;

    private FirebaseAPI() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        restaurantsCollection = db.collection("restaurants");
        productsCollection = db.collection("products");
        ingredientsCollection = db.collection("ingredients");
    }

    public static FirebaseAPI getInstance() {
        if (firebase == null) {
            firebase = new FirebaseAPI();
        }
        return firebase;
    }

    public void getShops(OnSuccessListener<List<DocumentSnapshot>> callback) {
        Query query = restaurantsCollection;
        executeDocumentListQuery(callback, query);
    }
    public void getProducts(String shopId ,OnSuccessListener<List<DocumentSnapshot>> callback) {
        Query query = productsCollection.whereEqualTo("shopId", shopId);
        executeDocumentListQuery(callback, query);
    }
    public void getIngredients(String productId ,OnSuccessListener<List<DocumentSnapshot>> callback) {
        Query query = ingredientsCollection.whereEqualTo("productId", productId);
        executeDocumentListQuery(callback, query);
    }
    private void executeDocumentListQuery(OnSuccessListener<List<DocumentSnapshot>> callback, Query query) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    callback.onSuccess(querySnapshot.getDocuments());
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
}

package za.simshezi.shop.api;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.model.ShopModel;

public class FirebaseAPI {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private CollectionReference restaurantsCollection;
    private CollectionReference productsCollection;
    private CollectionReference ingredientsCollection;
    private CollectionReference ordersCollection;
    private static FirebaseAPI firebase;

    private FirebaseAPI() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        restaurantsCollection = db.collection("restaurants");
        productsCollection = db.collection("products");
        ingredientsCollection = db.collection("ingredients");
        ordersCollection = db.collection("orders");
    }

    public static FirebaseAPI getInstance() {
        if (firebase == null) {
            firebase = new FirebaseAPI();
        }
        return firebase;
    }

    public List<ShopModel> getShops() {
        List<ShopModel> shops = new ArrayList<>();
        Query query = restaurantsCollection;
        executeDocumentListQuery(query, list -> {
            if (list != null) {
                for (DocumentSnapshot documentSnapshot : list) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        String cellphone = documentSnapshot.getString("cellphone");
                        String email = documentSnapshot.getString("email");
                        boolean status = documentSnapshot.getBoolean("status");
                        double rating = documentSnapshot.getDouble("rating");
                        getShopImage(id, bytes -> {
                            if (bytes != null) {
                                ShopModel model = new ShopModel(id, name, email, cellphone, (float) rating, status, bytes);
                                shops.add(model);
                            }
                        });
                    }
                }
            }
        });
        return shops;
    }

    public List<ProductModel> getProducts(String shopId) {
        List<ProductModel> products = new ArrayList<>();
        Query query = productsCollection.whereEqualTo("shopId", shopId);
        executeDocumentListQuery(query, list ->{
            if(list != null){
                for(DocumentSnapshot documentSnapshot: list){
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String productId = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        double price = documentSnapshot.getDouble("price");
                        getProductImage(productId, bytes -> {
                            if (bytes != null) {
                                ProductModel model = new ProductModel(productId, shopId, name, description, (float) price, bytes);
                                products.add(model);
                            }
                        });
                    }
                }
            }
        });
        return products;
    }

    public List<IngredientModel> getIngredients(String productId) {
        List<IngredientModel> ingredients = new ArrayList<>();
        Query query = ingredientsCollection.whereEqualTo("productId", productId);
        executeDocumentListQuery(query, list -> {
            if(list != null){
                for(DocumentSnapshot documentSnapshot: list){
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String ingredientId = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        double price = documentSnapshot.getDouble("price");
                        ingredients.add(new IngredientModel(ingredientId, productId, name, (float) price));
                    }
                }
            }
        });
        return ingredients;
    }

    private void executeDocumentListQuery(Query query, OnSuccessListener<List<DocumentSnapshot>> callback) {
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

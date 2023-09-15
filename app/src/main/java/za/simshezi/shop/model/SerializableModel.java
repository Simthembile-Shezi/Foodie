package za.simshezi.shop.model;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableModel {
    Serializable getModel();
}

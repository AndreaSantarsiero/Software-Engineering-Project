package it.polimi.ingsw.gc11.model.shipcard;

import it.polimi.ingsw.gc11.model.Material;
import java.util.ArrayList;
import java.util.List;

public class Storage extends ShipCard {

    public enum Type{
        DOUBLEBLUE, TRIPLEBLUE, SINGLERED, DOUBLERED;
    }

    private Type type;
    private List<Material> materials;


    public Storage(Connector topConnector, Connector rightConnector, Connector bottomConnector, Connector leftConnector, Type type) {
        super(topConnector, rightConnector, bottomConnector, leftConnector);
        this.type = type;
    }


    public Type getType() {
        return type;
    }


    public void addMaterial(Material newMaterial) {
        if (newMaterial == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (type.equals(Type.SINGLERED) && !materials.isEmpty()){
            throw new StorageAlreadyFullException("Storage is already full. Use replaceMaterial method");
        }
        if ((type.equals(Type.DOUBLERED) || type.equals(Type.DOUBLEBLUE)) && materials.size() > 1){
            throw new StorageAlreadyFullException("Storage is already full. Use replaceMaterial method");
        }
        if (type.equals(Type.TRIPLEBLUE) && materials.size() > 2){
            throw new StorageAlreadyFullException("Storage is already full. Use replaceMaterial method");
        }
        if(newMaterial.getType() == Material.Type.RED){
            if(type.equals(Type.DOUBLERED) || type.equals(Type.SINGLERED)) {
                materials.add(newMaterial);
            }
            else {
                throw new IllegalArgumentException("Can't add red material to a blue storage");
            }
        }
        else{
            materials.add(newMaterial);
        }
    }

    public void removeMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (materials.isEmpty()) {
            throw new ShipCardNotFoundException("Material not found");
        }
    }

    public void replaceMaterial(Material newMaterial, Material oldMaterial) {
        if (newMaterial == null || oldMaterial == null) {
            throw new IllegalArgumentException("Material is null");
        }
        if (materials.isEmpty()) {
            throw new StorageStillEmptyException("Storage full, nothing to replace");
        }
    }
}

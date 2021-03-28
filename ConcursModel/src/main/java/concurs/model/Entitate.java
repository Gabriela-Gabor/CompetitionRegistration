package concurs.model;

import java.io.Serializable;

public class Entitate<ID> implements Serializable {

    private ID id;
    public ID getId(){return id;}
    public void setId(ID id){this.id=id;}

}

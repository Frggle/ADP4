package adt;

public class HashObject {

    private String value;
    private int kollision;
    private int vorkommen;

    public HashObject(String value) {
        this.value = value;
        this.vorkommen = 1;
        this.kollision = 0;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the kollision
     */
    public int getKollision() {
        return kollision;
    }

    public void inkrementKollision() {
        this.kollision++;
    }
    
    public void dekrementKollision() {
        if(this.kollision > 0) {
            this.kollision--;
        }
    }

    /**
     * @return the Vorkommen
     */
    public int getVorkommen() {
        return vorkommen;
    }

    public void inkrementVorkommen() {
        this.vorkommen++;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + kollision;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + vorkommen;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof HashObject))
            return false;
        HashObject other = (HashObject) obj;
        if (kollision != other.kollision)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (vorkommen != other.vorkommen)
            return false;
        return true;
    }
    
    

}

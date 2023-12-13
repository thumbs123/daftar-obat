package model;

public class Data {

    private String id, nama, deskripsi, imagePath;

    // Konstruktor
    public Data(String id, String nama, String deskripsi, String imagePath) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
    }

    // Getter dan setter untuk atribut imagePath
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getter dan setter lainnya
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}

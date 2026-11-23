package com.studentmarket.model;

public class ListingImage {

    private final int id;
    private final int listingId;
    private final String url;      
    private final int urutan;

    ListingImage(int id, int listingId, String url, int urutan) {
        this.id = id;
        this.listingId = listingId;
        this.url = url;
        this.urutan = urutan;
    }

    public int getId() { return id; }
    public int getListingId() { return listingId; }
    public String getUrl() { return url; }
    public int getUrutan() { return urutan; }
}

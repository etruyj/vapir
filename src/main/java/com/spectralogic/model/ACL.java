//================================= ACL ====================================
// Description:
// This class represents an Access Control List (ACL) entity.
//
// Variables:
// - type: The type of the ACL.
// - id: The ID of the ACL.
// - read: Indicates if read access is granted.
// - readACP: Indicates if read access control policy is granted.
// - write: Indicates if write access is granted.
// - writeACP: Indicates if write access control policy is granted.
//
// Created by Sean Snyder.
//===========================================================================

package com.spectralogic.vail.vapir.model;

public class ACL {
    private String type;
    private String id;
    private boolean read;
    private boolean readACP;
    private boolean write;
    private boolean writeACP;

    // Constructors
    public ACL() {
    }

    public ACL(String type, String id, boolean read, boolean readACP, boolean write, boolean writeACP) {
        this.type = type;
        this.id = id;
        this.read = read;
        this.readACP = readACP;
        this.write = write;
        this.writeACP = writeACP;
    }

    //=============================
    // Getters
    //=============================
    public String getType() { return type; }
    public String getId() { return id; }
    public boolean isRead() { return read; }
    public boolean isReadACP() { return readACP; }
    public boolean isWrite() { return write; }
    public boolean isWriteACP() { return writeACP; }
    //=============================

    //=============================
    // Setters
    //=============================
    public void setType(String type) { this.type = type; }
    public void setId(String id) { this.id = id; }
    public void setRead(boolean read) { this.read = read; }
    public void setReadACP(boolean readACP) { this.readACP = readACP; }
    public void setWrite(boolean write) { this.write = write; }
    public void setWriteACP(boolean writeACP) { this.writeACP = writeACP; }
    //=============================
}

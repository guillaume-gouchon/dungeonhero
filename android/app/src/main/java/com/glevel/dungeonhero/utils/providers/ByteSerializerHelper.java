package com.glevel.dungeonhero.utils.providers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ByteSerializerHelper {

    /**
     * Gets an object from a byte array.
     *
     * @param blob
     * @return
     */
    public static Serializable getObjectFromByte(byte[] blob) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            ObjectInputStream in = new ObjectInputStream(bais);
            Serializable o = (Serializable) in.readObject();
            in.close();
            return o;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert any object to a byte array object.
     *
     * @param object
     * @return
     */
    public static byte[] toByteArray(Serializable object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutput out = new ObjectOutputStream(baos);
            out.writeObject(object);
            out.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

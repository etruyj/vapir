//===================================================================
// XmlParser.java
//      Description:
//          Imports and exports Xml documents.
//===================================================================

package com.socialvagrancy.vail.utils.io;

import java.io.File;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class XmlParser {
    
    public static void saveDoc(String file_path, Object object) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File output = new File(file_path);
            marshaller.marshal(object, output);
        } catch(JAXBException e) {
            e.printStackTrace();
        }
    }
}

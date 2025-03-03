//===================================================================
// SerializeEndpoint.java
// 	Description:
// 		Converts ArrayList<Endpoint> to an ArrayList<Output>
// 		for display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.Endpoint;
import com.spectralogic.vail.vapir.model.OutputFormat;
import java.util.ArrayList;

public class SerializeEndpoint
{
	public static ArrayList<OutputFormat> forOutput(ArrayList<Endpoint> list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line; 

        for(Endpoint endpoint : list) {
            line = new OutputFormat();
            line.setHeader("endpoint");
            line.setValue(null);
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>id");
            line.setValue(endpoint.getId());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>name");
            line.setValue(endpoint.getName());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>type");
            line.setValue(endpoint.getType());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>location");
            line.setValue(endpoint.getLocation());
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>version");
            if(endpoint.getVersion() != null) {
                line.setValue(endpoint.getVersion());
            } else {
                line.setValue("");
            }
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>url");
            if(endpoint.getUrl() != null) {    
                line.setValue(endpoint.getUrl());
            } else {
                line.setValue("");
            }
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>management_url");
            if(endpoint.getManagementURL() != null) {    
                line.setValue(endpoint.getManagementURL());
            } else {
                line.setValue("");
            }
            output.add(line);

            line = new OutputFormat();
            line.setHeader("endpoint>state");
            line.setValue(endpoint.getStatus());
            output.add(line);

            // Row End Marker
            line = new OutputFormat();
            line.setHeader("endpoint");
            line.setValue(null);
            output.add(line);
        }

        // Final Row End Marker
        line = new OutputFormat();
        line.setHeader("endpoint");
        line.setValue(null);
        output.add(line);

		return output;
	}
}

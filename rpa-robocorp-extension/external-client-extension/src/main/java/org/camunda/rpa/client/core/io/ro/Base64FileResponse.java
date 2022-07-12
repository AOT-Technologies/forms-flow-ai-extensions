package org.camunda.rpa.client.core.io.ro;
import java.io.Serializable;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Base64FileResponse implements IRO{

	private static final long serialVersionUID = 1L;

    public Base64FileResponse(String name, String url, Long size) {
        this.name = name;
        this.url = "data:application/pdf;base64,"+url;
        this.size = size;
    }

    private String storage = "base64";
    private String name;
    private String url;

    private Long size;

    @Override
    public String toString() {
        return "Base64FileResponse{" +
                "storage='" + storage + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                '}';
    }
}

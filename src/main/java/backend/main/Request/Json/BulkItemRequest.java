package backend.main.Request.Json;

import java.util.List;

public class BulkItemRequest {
    private List<ItemProductJson> items;

    public List<ItemProductJson> getItems() {
        return items;
    }

    public void setItems(List<ItemProductJson> items) {
        this.items = items;
    }
}



package pizzashop.repository;

import pizzashop.model.MenuDataModel;

import java.io.IOException;
import java.util.List;

public interface IMenuRepository {
    void readMenu() throws IOException;
     MenuDataModel getMenuItem(String line);
    List<MenuDataModel> getMenu() throws IOException;
}
package pizzashop.repository;

import org.apache.log4j.Logger;
import pizzashop.model.MenuDataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MenuRepository implements IMenuRepository{
    private static String filename = "data/menu.txt";
    private List<MenuDataModel> listMenu;
    static Logger log = Logger.getLogger(MenuRepository.class.getName());


    public MenuRepository(){
        //default repository
    }

    public void readMenu() throws IOException {
        ClassLoader classLoader = MenuRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        this.listMenu= new ArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                MenuDataModel menuItem=getMenuItem(line);
                if(menuItem!=null)
                    listMenu.add(menuItem);
            }
            br.close();

        } catch (FileNotFoundException e) {
            log.debug("Read Menu error: File not found");

        } catch (IOException a) {
            log.debug("Read Menu error");
        }if (br != null) {
            // again, a resource is involved, so try-catch another time
            try {
                br.close();
            } catch (IOException e) {
                log.debug("IoException error");
            }
        }

    }

    public MenuDataModel getMenuItem(String line){
        MenuDataModel item=null;
        if (line==null|| line.equals("")) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        String name= st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        item = new MenuDataModel(name, 0, price);
        return item;
    }

    public List<MenuDataModel> getMenu() throws IOException {
        readMenu();//create a new menu for each table, on request
        return listMenu;
    }

}

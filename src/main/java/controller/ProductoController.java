package controller;

import com.mycompany.proyectotiendaonlinejsf.Cambios;
import com.mycompany.proyectotiendaonlinejsf.Categoria;
import com.mycompany.proyectotiendaonlinejsf.Imagen;
import com.mycompany.proyectotiendaonlinejsf.Producto;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import facade.ImagenFacade;
import facade.ProductoFacade;

import java.io.Serializable;
import static java.lang.System.out;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("productoController")
@SessionScoped
public class ProductoController implements Serializable {
    private facade.CategoriaFacade ejbCFacade;
    private Producto current;
    @EJB
    private facade.ImagenFacade imagenFacade;
    @EJB
    private facade.ProductoFacade ejbFacade;
    private FileUpload fu;
    private Double p = null;
    private Character b;
    private Integer idpedido;
    private String busqueda = "";
    private String opcion = "";
    private DataModel items = null;
    private List<Imagen> listaimagenes;
    private Producto selectedProducto;
    private String selected="";
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProductoController() {
        fu=new FileUpload();
        current = new Producto();
    }
    public void buscar() {
        items = null;
        if (getBusqueda().trim().equals("")) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };

        }else{
            switch(opcion){
                case "marca":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyMarca(getBusqueda()));
                        }
                    };
                    break;
                case "id":
                    try{
                        idpedido=Integer.valueOf(getBusqueda());
                    }catch(Exception ex){
                        FacesMessage message = new FacesMessage("El valor del id es incorrecto");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyId(idpedido));
                        }
                    };
                    break;
                case "precio":
                    try{
                        p=Double.valueOf(getBusqueda());
                    }catch(Exception ex){
                        FacesMessage message = new FacesMessage("El valor de precio es incorrecto");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyPrecio(p));
                        }
                    };
                    break;
                    
                case "estado":
                    if (getBusqueda().trim().toLowerCase().equals("disponible"))
                        b='A';
                    if (getBusqueda().trim().toLowerCase().equals("no disponible"))
                        b='D';
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyEstado(b));
                        }
                    };
                    break;
            }
            
        }
        
    }
    public void save(Imagen im){
        int count = imagenFacade.findbyID(im.getIdproducto()).size();
        if (count==0)
            im.setDescripcion("BUSQUEDA");
        else
            im.setDescripcion("DETALLE");
//        out.println(">>>>>>>>>>>"+getDescImagen());
//        im.setDescripcion(getDescImagen().trim().toUpperCase());
        imagenFacade.create(im);
        Cambios.ins().addNI(im.getIdimagen());
    }
    public Producto getSelected() {
        if (current == null) {
            current = new Producto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Producto) getItems().getRowData();
        listaimagenes=imagenFacade.findbyID(current);
        fu.setCurrent(current);
        fu.setIns(this);
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    public void realoadImages() {
        out.println(">>>>>>>>>>>>>>>>>>>>>>>Reload");
        listaimagenes=imagenFacade.findbyID(current);
    }

    public String prepareCreate() {
        current = new Producto();
        selectedItemIndex = -1;
        return "Create.xhtml";
    }
    
    public void prepareCreate1(){
        current = new Producto();
        selectedItemIndex = -1;
    }

    public String create() {
        out.print(current);
        try {
            items=null;
            getFacade().create(current);
            Cambios.ins().addNP(current.getIdProducto());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Producto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            Cambios.ins().addUP(current.getIdProducto());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Producto) getItems().getRowData();
        if (current.getEstado().equals('D')){
            current.setEstado('A');
        }else{
            current.setEstado('D');
        }
        update();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
//        performDestroy();
//        recreatePagination();
//        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Producto getProducto(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Producto getSelectedProducto() {
        return selectedProducto;
    }

    public void setSelectedProducto(Producto selectedProducto) {
        this.selectedProducto = selectedProducto;
    }

    public List<Imagen> getListaimagenes() {
        return listaimagenes;
    }

    public void setListaimagenes(List<Imagen> listaimagenes) {
        this.listaimagenes = listaimagenes;
    }

    public FileUpload getFu() {
        return fu;
    }

    public void setFu(FileUpload fu) {
        this.fu = fu;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public facade.ProductoFacade getEjbFacade() {
        return ejbFacade;
    }

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
            return controller.getProducto(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdProducto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Producto.class.getName());
            }
        }

    }

}

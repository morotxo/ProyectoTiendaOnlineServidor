package controller;

import com.mycompany.proyectotiendaonlinejsf.Cliente;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import facade.ClienteFacade;

import java.io.Serializable;
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

@Named("clienteController")
@SessionScoped
public class ClienteController implements Serializable {

    private Cliente current;
    private DataModel items = null;
    @EJB
    private facade.ClienteFacade ejbFacade;
    private String busqueda="";
    private String opcion="";
    private Character ca;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer idcliente;

    public ClienteController() {
    }

    public void setSelected(Cliente c) {
        this.current=c;
    }
    public Cliente getSelected() {
        if (current == null) {
            current = new Cliente();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public void buscar(){
        setBusqueda(getBusqueda().trim());
        items=null;
        if(getBusqueda().equals("")){
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
                case "ciudad":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyCiudad(getBusqueda()));
                        }
                    };
                    break;
                case "apellido":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyApellido(getBusqueda()));
                        }
                    };
                    break;
                case "email":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyEmail(getBusqueda()));
                        }
                    };
                    break;
                case "estado":
                    
                    if (getBusqueda().toLowerCase().equals("activo")){
                        ca='A';
                    }else if (getBusqueda().trim().toLowerCase().equals("no activo")){
                        ca='I';
                    }
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyEstado(ca));
                        }
                    };
                    break;
                case "idcliente":
                    try{
                        idcliente=Integer.valueOf(getBusqueda());
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
                            return new ListDataModel(getFacade().findbyIdCliente(idcliente));
                        }
                    };
                    break;
                case "nombre":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyNombre(getBusqueda()));
                        }
                    };
                    break;
                case "pais":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyPais(getBusqueda()));
                        }
                    };
                    break;
                case "telefono":
                    pagination = new PaginationHelper(10) {
                        @Override
                        public int getItemsCount() {
                            return getFacade().count();
                        }
                        @Override
                        public DataModel createPageDataModel() {
                            return new ListDataModel(getFacade().findbyTelefono(getBusqueda()));
                        }
                    };
                    break;
            }
        }
        
    }

    private ClienteFacade getFacade() {
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
        current = (Cliente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cliente();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Cliente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cliente) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClienteDeleted"));
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

    public Cliente getCliente(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    @FacesConverter(forClass = Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
            return controller.getCliente(getKey(value));
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
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getIdCliente());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cliente.class.getName());
            }
        }

    }

}

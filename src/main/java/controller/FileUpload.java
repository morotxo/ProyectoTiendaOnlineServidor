package controller;

import com.mycompany.proyectotiendaonlinejsf.Imagen;
import com.mycompany.proyectotiendaonlinejsf.Producto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
public class FileUpload{
    @EJB
    private facade.ImagenFacade imF;
    private ProductoController ins;
    private Producto current;
    private String descImagen;
    private String nombreImagen;
    
    public FileUpload(){
    }
    public void handleFileUpload(FileUploadEvent event) {
        out.println("DescImagen:::::::::"+getDescImagen());
        setNombreImagen(event.getFile().getFileName());
        try {
           
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext();
            String path= "/media/david13mo/DARK 700/Universidad/6. Programacion Web/12. Trabajo Final/ProyectoTiendaOnlineJSF/src/main/webapp/resources/Imagenes/"+getCurrent().getIdProducto()+"/";
            File targetFolder = new File(path);
            targetFolder.mkdir();
            String name=event.getFile().getFileName().trim().replace(' ', '_');
            name=name.replace('-','_');
            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder,name));
            int read = 0;
            byte[] bytes = new byte[1024];
            System.out.println(targetFolder.getAbsolutePath());
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            saveImage(event,path,name);
            FacesMessage message = new FacesMessage("Se ha subido", event.getFile().getFileName());
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException e) {
            FacesMessage message = new FacesMessage("Ha ocurrido un error subiendo la imagen "+e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
            
        }
    }
    
    
    public void saveImage(FileUploadEvent event, String path,String name){
        String m = String.valueOf(System.currentTimeMillis());
        Integer id=Math.abs(m.hashCode());
        Imagen im=new Imagen();
        im.setUrl("/resources/Imagenes/"+getCurrent().getIdProducto()+"/"+name);
        im.setDescripcion(event.getFile().getFileName()+" "+event.getFile().getContentType());
        im.setIdimagen(id);
        im.setIdproducto(current);
        System.out.println(im);
        System.out.println(im.getDescripcion());
        System.out.println(im.getIdimagen());
        System.out.println(im.getIdproducto());
        System.out.println(im.getUrl());
        ins.save(im);
        ins.realoadImages();
//        ImagenController imC=new ImagenController();
//        imC.prepareCreate();
//        imC.setCurrent(im);
//        imC.create();
//        imF.findAll();
//        imF.create(im);
//        System.out.println("Creado Exitosamente");
    }
//    private void directorio(String path){
//        if (!Files.exists(path)) {
//        }
//    }

    

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public Producto getCurrent() {
        return current;
    }

    public void setCurrent(Producto current) {
        this.current = current;
    }

    void setIns(ProductoController aThis) {
        this.ins=aThis;
    }

    public String getDescImagen() {
        return descImagen;
    }

    public void setDescImagen(String descImagen) {
        this.descImagen = descImagen;
    }


   
}

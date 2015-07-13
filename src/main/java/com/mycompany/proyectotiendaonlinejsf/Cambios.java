package com.mycompany.proyectotiendaonlinejsf;


import com.mycompany.proyectotiendaonlinejsf.Producto;
import controller.ImagenController;
import controller.ProductoController;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david13mo
 */
public class Cambios {
    private List<Integer> productoU;
    private List<Integer> productoN;
    private List<Integer> imagenU;
    private List<Integer> imagenN;
    private List<Producto> listadevolverP;
    private List<Imagen> listadevolverI;
    private ProductoController pC;
    private ImagenController iC;
    private static Cambios ins;
    private Cambios(){
        productoU=new ArrayList<>();
        productoN=new ArrayList<>();
        imagenU=new ArrayList<>();
        imagenN=new ArrayList<>();
        listadevolverP=new ArrayList<>();
    }
    
    public void addNP(Integer producto){
        productoN.add(producto);
    }
    
    public void addUP(Integer producto){
        productoU.add(producto);
    }
    public void addNI(Integer imagen){
        productoN.add(imagen);
    }
    
    public void addUI(Integer imagen){
        productoU.add(imagen);
    }
    
    public static Cambios ins(){
        if (ins==null)
            ins=new Cambios();
        return ins;
    }
    
    public List<Producto>  getUpdates(){
        listadevolverP=new ArrayList<>();
        for (Integer idP : productoN) {
            Producto find = pC.getEjbFacade().find(idP);
            listadevolverP.add(find);
        }
        productoN=new ArrayList<>();
        return listadevolverP;
    }
    
    public List<Producto>  getNews(){
        listadevolverP=new ArrayList<>();
        for (Integer idP : productoU) {
            Producto find = pC.getEjbFacade().find(idP);
            listadevolverP.add(find);
        }
        productoU=new ArrayList<>();
        return listadevolverP;
    }
    public List<Imagen>  getIUpdates(){
        listadevolverI=new ArrayList<>();
        for (Integer idI : imagenN) {
            Imagen find = iC.getEjbFacade().find(idI);
            listadevolverI.add(find);
        }
        imagenN=new ArrayList<>();
        return listadevolverI;
    }
    
    public List<Imagen>  getINews(){
        listadevolverI=new ArrayList<>();
        for (Integer idI : imagenU) {
            Imagen find = iC.getEjbFacade().find(idI);
            listadevolverI.add(find);
        }
        productoU=new ArrayList<>();
        return listadevolverI;
    }

    public List<Producto> getListadevolverP() {
        return listadevolverP;
    }
    

    public List<Imagen> getListadevolverI() {
        return listadevolverI;
    }
    
    
    
}

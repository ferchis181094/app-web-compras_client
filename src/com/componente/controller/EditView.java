/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.componente.controller;

import com.eCommerce.controller.ReglasController;
import com.eCommerce.entity.Regla;
import java.io.Serializable;
import java.util.List;
import javafx.scene.control.TableColumn.CellEditEvent;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author rolan
 */
@ManagedBean(name = "dtEditView")
@ViewScoped
public class EditView implements Serializable {

    private List<Regla> regla1;

    private ReglasController service;

    @PostConstruct
    public void init() {
        service = new ReglasController();
        regla1 = service.selectAll();

    }

    public List<Regla> getRegla1() {
        return regla1;
    }

    public void setRegla1(List<Regla> regla1) {
        this.regla1 = regla1;
    }

    public void onRowEdit(RowEditEvent<Regla> event) {
        service.update(event.getObject());
        FacesMessage msg = new FacesMessage("Product Edited", String.valueOf(event.getObject().getIdReglas()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent<Regla> event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(event.getObject().getIdReglas()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}

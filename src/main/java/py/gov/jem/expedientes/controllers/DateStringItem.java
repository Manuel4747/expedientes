/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DateStringItem {
    private String date;

    /**
     * @return the date
     */
    @Id
    @Column(name = "DATE_VALUE")
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
}

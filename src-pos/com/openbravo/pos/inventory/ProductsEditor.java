//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.inventory;

import java.util.List;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.CategoryInfo;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author adrianromero
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class ProductsEditor extends JPanel implements EditorRecord {
       
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;

    private SentenceList m_senttax;
    private ComboBoxValModel m_TaxModel;  
    
    private SentenceList m_sentmat;
    private ComboBoxValModel m_MaterialModel;
    
    private ComboBoxValModel m_MatListModel;
    private List<MaterialProdInfo> m_matlist;
    private MaterialProdInfo m_selMaterial = null;
    
    private ComboBoxValModel m_CodetypeModel;
    
    DirtyManager m_dirty = null;
    DataLogicSales m_dlSales;
    
    private Object m_id;
    private Double m_dPriceSell;
    
    /** Creates new form JEditProduct */
    public ProductsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();
        m_dlSales = dlSales;
             
        m_matlist = new ArrayList();
             
        // El modelo de categorias
        m_sentcat = dlSales.getCategoriesProductsList();
        m_CategoryModel = new ComboBoxValModel();
        
        // El modelo de impuestos
        m_senttax = dlSales.getTaxList();
        m_TaxModel = new ComboBoxValModel();
        
        m_MaterialModel = new ComboBoxValModel();
        m_MatListModel = new ComboBoxValModel();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
               
        m_jRef.getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        m_dirty = dirty;

        m_jMaterial.addActionListener(new SelectedMaterial());
        
        // el informe de stock
        ReportManager rm = new ReportManager();
        m_jPriceBuy.getDocument().addDocumentListener(rm);
        m_jTax.addActionListener(rm);
        m_jstockcost.getDocument().addDocumentListener(rm);
        m_jstockvolume.getDocument().addDocumentListener(rm);
        
        m_jPriceSell.getDocument().addDocumentListener(new PriceManager());
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        m_jmargin.getDocument().addDocumentListener(new MarginManager());
        
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        CategoryInfo catinfo;
        List catlist = m_sentcat.list();
        
        //Eliminamos de la lista de categorias la categoria Composiciones
        for (int i=0; i < catlist.size(); i++) {
            catinfo = (CategoryInfo)catlist.get(i);
            if (catinfo.getID().equals("0")) {
                catlist.remove(i);
                break;
            } 
        }
        
        m_CategoryModel = new ComboBoxValModel(catlist);
        m_jCategory.setModel(m_CategoryModel);

        m_TaxModel = new ComboBoxValModel(m_senttax.list());
        m_jTax.setModel(m_TaxModel);
        
        // Obtenemos la lista de materiales
        List<MaterialProdInfo> list = m_dlSales.getMaterialsProd(null);
        m_MaterialModel = new ComboBoxValModel(list);
        m_jMaterial.setModel(m_MaterialModel);
    }
    
    public void writeValueEOF() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_TaxModel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_bPriceSellLock = true;
        m_jPriceSell.setText(null);            
        m_bPriceSellLock = false;
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(false);
        m_jBreakdown.setSelected(false);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jBreakdown.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        
//        m_dUnits = null;
        writeReport(null, 0.0);
    }
    public void writeValueInsert() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_TaxModel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_dPriceSell = null;
        m_bPriceSellLock = true;
        m_jPriceSell.setText(null);            
        m_bPriceSellLock = false;
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jBreakdown.setSelected(false);
        m_jInCatalog.setSelected(true);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jBreakdown.setEnabled(true);
        m_jInCatalog.setEnabled(true); 
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(true);

//        m_dUnits = null;
        writeReport(null, 0.0);
   }
    public void writeValueDelete(Object value) {
        
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        m_dPriceSell = (Double) myprod[7];
        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;
        m_CategoryModel.setSelectedKey(myprod[8]);
        m_TaxModel.setSelectedKey(myprod[9]);
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_jInCatalog.setSelected(((Boolean)myprod[13]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[14]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[15]));
        txtAttributes.setCaretPosition(0);
        m_jBreakdown.setSelected(false);
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jBreakdown.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);

        TaxInfo tax = (TaxInfo) m_TaxModel.getElementByKey(myprod[9]);
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();      
        writeReport((Double) myprod[6], dTaxRate);
    }    
    
    public void writeValueEdit(Object value) {
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_dPriceSell = (Double) myprod[7];
        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;
        m_CategoryModel.setSelectedKey(myprod[8]);
        m_TaxModel.setSelectedKey(myprod[9]);
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_jInCatalog.setSelected(((Boolean)myprod[13]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[14]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[15]));
        txtAttributes.setCaretPosition(0);
        fillMaterialList();
        if (m_matlist.size() > 0) {
            writePriceBuy();
            m_jBreakdown.setSelected(true);
        } else {
            m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
            m_jBreakdown.setSelected(false);
        }
        
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jBreakdown.setEnabled(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());  
        txtAttributes.setEnabled(true);
        
        TaxInfo tax = (TaxInfo) m_TaxModel.getElementByKey(myprod[9]);
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();   
        writeReport((Double) myprod[6], dTaxRate);
    }

    public Object createValue() throws BasicException {
        Object[] myprod = new Object[16 + (m_matlist.size() * 2) +1];
        myprod[0] =  m_id == null ? UUID.randomUUID().toString() : m_id;
        myprod[1] = m_jRef.getText();
        myprod[2] = m_jCode.getText();
        myprod[3] = m_jName.getText();
        myprod[4] = Boolean.valueOf(m_jComment.isSelected());
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
        myprod[7] = m_dPriceSell; // Formats.CURRENCY.parseValue(m_jPriceSell.getText());
        myprod[8] = m_CategoryModel.getSelectedKey();
        myprod[9] = m_TaxModel.getSelectedKey();
        myprod[10] = m_jImage.getImage();
        myprod[11] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
        myprod[12] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
        myprod[13] = Boolean.valueOf(m_jInCatalog.isSelected());       
        myprod[14] = Formats.INT.parseValue(m_jCatalogOrder.getText()); 
        myprod[15] = Formats.BYTEA.parseValue(txtAttributes.getText());
        
        //Añadimos los datos de los materiales
        int i = 16;
        myprod[i] = m_matlist.size();
        for (MaterialProdInfo m: m_matlist) {
            myprod[++i] = m.getID();
            myprod[++i] = m.getAmount();
        }
        
        return myprod;
    }    
    
    public Component getComponent() {
        return this;
    }

    private void writeReport() {
        Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
        TaxInfo tax = (TaxInfo) m_TaxModel.getSelectedItem();
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();  
        writeReport(dPriceBuy, dTaxRate);
    }
    
    private void writePriceTax() {
        Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  
        TaxInfo tax = (TaxInfo) m_TaxModel.getSelectedItem();
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();  
        if (dPriceSellTax == null) {
            m_dPriceSell = null;
        } else {
            m_dPriceSell = new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate));
        }

        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;

        m_bPriceSellTaxWriteLock = true;
        writeReport();
        m_bPriceSellTaxWriteLock = false;
    }
    
    
    private void writePriceBuy() {
        Double dPriceBuy = 0.0;
        
        for (MaterialProdInfo m: m_matlist) {
            dPriceBuy += m.getAmount() * m.getPriceBuy();
        }
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(dPriceBuy));            

        m_bPriceSellTaxWriteLock = true;
        writeReport();
        m_bPriceSellTaxWriteLock = false;
    }
    
    private void writeMargin() {
        Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
        Double dMargin = readPercent(m_jmargin.getText());  
        if (dMargin == null || dPriceBuy == null) {
            m_dPriceSell = dPriceBuy;
        } else {
            m_dPriceSell = new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue()));
        }

        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;

        m_bMarginWriteLock = true;
        writeReport();
        m_bMarginWriteLock = false;
    }
    
    private boolean m_bPriceSellLock = false;
    private boolean m_bMarginLock = false;
    private boolean m_bPriceSellTaxLock = false;
    
    private boolean m_bMarginWriteLock = false;
    private boolean m_bPriceSellTaxWriteLock = false;

    
    // private void writeReport(Double dPriceBuy, double dTaxRate, Double dStockSecurity, Double dStockCost, Double dStockVolume) {
    private void writeReport(Double dPriceBuy, double dTaxRate) {
        if (!m_bPriceSellTaxWriteLock) { 
            m_bPriceSellTaxLock = true;
            if (m_dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(m_dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }      
            m_bPriceSellTaxLock = false;
        }
        if (!m_bMarginWriteLock) {
            m_bMarginLock = true;
            if (dPriceBuy == null || m_dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(new Double(m_dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
            }
            m_bMarginLock = false;
        }
    }
    
    private class ReportManager implements DocumentListener, ActionListener {
        public void changedUpdate(DocumentEvent e) {
            writeReport();
        }
        public void insertUpdate(DocumentEvent e) {
            writeReport();
        }    
        public void removeUpdate(DocumentEvent e) {
            writeReport();
        }         
        public void actionPerformed(ActionEvent e) {
            writeReport();
        }
    }
    
    private class PriceManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }         
    }
    
    private class PriceTaxManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }         
    }
    
    private class MarginManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }         
    }
    
    private class SelectedMaterial implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            m_selMaterial = ((MaterialProdInfo)m_jMaterial.getSelectedItem());
            m_jAmount.setText("1");
            try {
                m_jlblUnit.setText( m_selMaterial.getUnit() );
            } catch (NullPointerException ex) {
                m_jlblUnit.setText("");
            }
        }
    }
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    private void fillMaterialList() {
        String pid = (m_id != null) ? m_id.toString() : "";
        
        //Leemos y añadimos al JList los materiales del producto
        m_jMaterialsList.removeAll();
        if (m_id == null) {
            m_matlist.clear();
        } else {
            try {
                m_matlist = m_dlSales.getMaterialsProd(pid);
            } catch (BasicException e) {
                m_matlist.clear();
            }
        }
        
        m_MatListModel = new ComboBoxValModel(m_matlist);
        m_jMaterialsList.setModel(m_MatListModel);
        if (m_matlist.size() > 0) {
            m_jMaterialsList.setSelectedIndex(0);
            fireSelectedMaterial(m_matlist.get(0));
        }
        m_jMaterialsList.updateUI();
    }
        
    private void fireSelectedMaterial(MaterialProdInfo mat) {
        for (int i = 0; i < m_jMaterial.getItemCount(); i++) {
            if ( ((MaterialProdInfo)m_jMaterial.getItemAt(i)).getID().equals(mat.getID()) ) {
                m_jMaterial.setSelectedIndex(i);
        } 
    } 
    
        m_jlblUnit.setText( mat.getUnit() );
        m_jAmount.setText( Formats.DOUBLE.formatValue(mat.getAmount()) );
        
        //Establecemos el producto seleccionado
        m_selMaterial = mat;
    }
    
    
    private static class MyListData extends javax.swing.AbstractListModel {
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        public int getSize() {
            return m_data.size();
        } 
    } 
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        m_jCode = new javax.swing.JTextField();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        m_jmargin = new javax.swing.JTextField();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        m_jCodetype = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jstockvolume = new javax.swing.JTextField();
        m_jScale = new javax.swing.JCheckBox();
        m_jComment = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        m_jCatalogOrder = new javax.swing.JTextField();
        m_jInCatalog = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        m_jBreakdown = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();
        m_jPanelEscandallo = new javax.swing.JPanel();
        m_jAddMaterials = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        m_jMaterial = new javax.swing.JComboBox();
        m_jbtnAddMaterial = new javax.swing.JButton();
        m_jbtnDelMaterial = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        m_jAmount = new javax.swing.JTextField();
        m_jlblUnit = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jMaterialsList = new javax.swing.JList();

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(10, 50, 80, 17);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(180, 50, 70, 17);
        add(m_jRef);
        m_jRef.setBounds(90, 50, 70, 20);
        add(m_jName);
        m_jName.setBounds(250, 50, 220, 20);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 320, 30);

        jPanel1.setLayout(null);

        jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 20, 150, 17);
        jPanel1.add(m_jCode);
        m_jCode.setBounds(160, 20, 170, 20);

        m_jImage.setMaxDimensions(new java.awt.Dimension(64, 64));
        jPanel1.add(m_jImage);
        m_jImage.setBounds(340, 20, 200, 180);

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 50, 150, 17);

        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(160, 50, 80, 20);

        jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 80, 150, 17);

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell);
        m_jPriceSell.setBounds(160, 80, 80, 20);

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 170, 150, 17);
        jPanel1.add(m_jCategory);
        m_jCategory.setBounds(160, 170, 170, 20);

        jLabel7.setText(AppLocal.getIntString("label.prodtax")); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 140, 150, 17);
        jPanel1.add(m_jTax);
        m_jTax.setBounds(160, 140, 170, 20);

        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jmargin);
        m_jmargin.setBounds(250, 80, 80, 20);

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSellTax);
        m_jPriceSellTax.setBounds(160, 110, 80, 20);

        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 110, 150, 17);
        jPanel1.add(m_jCodetype);
        m_jCodetype.setBounds(250, 40, 80, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 20, 150, 17);

        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(160, 20, 80, 20);

        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 50, 150, 17);

        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockvolume);
        m_jstockvolume.setBounds(160, 50, 80, 20);
        jPanel2.add(m_jScale);
        m_jScale.setBounds(160, 140, 80, 22);
        jPanel2.add(m_jComment);
        m_jComment.setBounds(160, 110, 80, 22);

        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(250, 80, 60, 17);

        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(310, 80, 80, 20);

        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel2.add(m_jInCatalog);
        m_jInCatalog.setBounds(160, 80, 50, 22);

        jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 80, 150, 17);

        jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 110, 150, 17);

        jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
        jPanel2.add(jLabel12);
        jLabel12.setBounds(10, 140, 150, 17);

        m_jBreakdown.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                m_jBreakdownStateChanged(evt);
            }
        });
        m_jBreakdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBreakdownActionPerformed(evt);
            }
        });
        jPanel2.add(m_jBreakdown);
        m_jBreakdown.setBounds(160, 170, 80, 22);

        jLabel15.setText(AppLocal.getIntString("label.prodbreakdown")); // NOI18N
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 170, 150, 17);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(AppLocal.getIntString("label.attributes"), jPanel3); // NOI18N

        m_jPanelEscandallo.setBorder(null);
        m_jPanelEscandallo.setLayout(null);

        m_jAddMaterials.setMinimumSize(new java.awt.Dimension(100, 0));
        m_jAddMaterials.setPreferredSize(new java.awt.Dimension(252, 0));

        jLabel13.setText(AppLocal.getIntString("label.prodmaterial")); // NOI18N

        m_jbtnAddMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btngreenplus.png"))); // NOI18N
        m_jbtnAddMaterial.setFocusPainted(false);
        m_jbtnAddMaterial.setFocusable(false);
        m_jbtnAddMaterial.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnAddMaterial.setRequestFocusEnabled(false);
        m_jbtnAddMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnAddMaterialActionPerformed(evt);
            }
        });

        m_jbtnDelMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnredminus.png"))); // NOI18N
        m_jbtnDelMaterial.setFocusPainted(false);
        m_jbtnDelMaterial.setFocusable(false);
        m_jbtnDelMaterial.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnDelMaterial.setRequestFocusEnabled(false);
        m_jbtnDelMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnDelMaterialActionPerformed(evt);
            }
        });

        jLabel14.setText(AppLocal.getIntString("label.prodamount")); // NOI18N

        m_jAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jlblUnit.setText(AppLocal.getIntString("label.prodamount")); // NOI18N

        org.jdesktop.layout.GroupLayout m_jAddMaterialsLayout = new org.jdesktop.layout.GroupLayout(m_jAddMaterials);
        m_jAddMaterials.setLayout(m_jAddMaterialsLayout);
        m_jAddMaterialsLayout.setHorizontalGroup(
            m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddMaterialsLayout.createSequentialGroup()
                .addContainerGap()
                .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jAddMaterialsLayout.createSequentialGroup()
                        .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel13)
                            .add(m_jAddMaterialsLayout.createSequentialGroup()
                                .add(jLabel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(4, 4, 4)
                        .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jAddMaterialsLayout.createSequentialGroup()
                                .add(m_jAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(m_jlblUnit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                .addContainerGap())
                            .add(m_jMaterial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 215, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jAddMaterialsLayout.createSequentialGroup()
                        .add(m_jbtnAddMaterial)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jbtnDelMaterial)
                        .add(23, 23, 23))))
        );
        m_jAddMaterialsLayout.setVerticalGroup(
            m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddMaterialsLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jAddMaterialsLayout.createSequentialGroup()
                        .add(m_jMaterial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel14)
                            .add(m_jAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jlblUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel13))
                .add(74, 74, 74)
                .add(m_jAddMaterialsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jbtnDelMaterial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jbtnAddMaterial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21))
        );

        m_jPanelEscandallo.add(m_jAddMaterials);
        m_jAddMaterials.setBounds(220, 10, 320, 190);

        m_jMaterialsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jMaterialsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jMaterialsListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(m_jMaterialsList);

        m_jPanelEscandallo.add(jScrollPane2);
        jScrollPane2.setBounds(10, 10, 200, 190);

        jTabbedPane1.addTab("Breakdown", m_jPanelEscandallo);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 90, 560, 280);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
 
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);   
        } else {
            m_jCatalogOrder.setEnabled(false);   
            m_jCatalogOrder.setText(null);   
        }

    }//GEN-LAST:event_m_jInCatalogActionPerformed

    private void m_jbtnAddMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnAddMaterialActionPerformed
        try {
            if (m_selMaterial != null) {
                Double amount = (Double)Formats.DOUBLE.parseValue(m_jAmount.getText());
                m_selMaterial.setAmount(amount);
                
                if (m_matlist.contains(m_selMaterial)) {
                    MaterialProdInfo m = m_matlist.get(m_matlist.indexOf(m_selMaterial) );
                    m.setAmount(amount);
                }else {
                    m_matlist.add(m_selMaterial);
                }
                writePriceBuy();
                
                m_dirty.setDirty(true);
                m_jMaterialsList.updateUI();
            }
        } catch (Exception e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotaddmaterial"), e));
        }
    }//GEN-LAST:event_m_jbtnAddMaterialActionPerformed

    private void m_jbtnDelMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnDelMaterialActionPerformed
        if (m_selMaterial != null && m_matlist.contains(m_selMaterial) ) {
            m_matlist.remove(m_selMaterial.getID());
            m_MatListModel.del(m_selMaterial);
            writePriceBuy();
            
            m_jMaterialsList.updateUI();
            m_dirty.setDirty(true);
            
            if (m_matlist.size() > 0) {
                m_jMaterialsList.setSelectedIndex(0);
                m_jMaterialsListValueChanged( new javax.swing.event.ListSelectionEvent(this, 0, 0, false) );
            }
        }
    }//GEN-LAST:event_m_jbtnDelMaterialActionPerformed

    private void m_jMaterialsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jMaterialsListValueChanged
        if (!evt.getValueIsAdjusting()) {
            MaterialProdInfo m = (MaterialProdInfo) m_jMaterialsList.getSelectedValue();
            if (m != null) {
                fireSelectedMaterial(m);
            }
        }
    }//GEN-LAST:event_m_jMaterialsListValueChanged

    private void m_jBreakdownStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_m_jBreakdownStateChanged
        if (m_jBreakdown.isSelected()) {
            //Apaarece la pestaña del escandallo
            jTabbedPane1.addTab(AppLocal.getIntString("label.prodbreakdown"), m_jPanelEscandallo);
            fillMaterialList();
            
            //Inhabilitamos el precio de compra, ya que se calculara a partir de los
            //materiales del escandallo
            m_jPriceBuy.setEditable(false);
        } else {
            //Desaparece la pestaña del escandallo
            if (jTabbedPane1.getTabCount() > 3)
                jTabbedPane1.removeTabAt(3);
            m_matlist.clear();
            
            m_jPriceBuy.setEditable(true);
            m_dirty.setDirty(true);
        }
    }//GEN-LAST:event_m_jBreakdownStateChanged

    private void m_jBreakdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBreakdownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBreakdownActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel m_jAddMaterials;
    private javax.swing.JTextField m_jAmount;
    private javax.swing.JCheckBox m_jBreakdown;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private javax.swing.JCheckBox m_jComment;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private javax.swing.JCheckBox m_jInCatalog;
    private javax.swing.JComboBox m_jMaterial;
    private javax.swing.JList m_jMaterialsList;
    private javax.swing.JTextField m_jName;
    private javax.swing.JPanel m_jPanelEscandallo;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jRef;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JButton m_jbtnAddMaterial;
    private javax.swing.JButton m_jbtnDelMaterial;
    private javax.swing.JLabel m_jlblUnit;
    private javax.swing.JTextField m_jmargin;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables
    
}

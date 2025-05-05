package componente;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Componente extends JButton {

    public Componente() {
        super("Generar Comprobante PDF");
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addActionListener(e -> generarPDF());
    }

    private void generarPDF() {
        try {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (!(window instanceof JFrame)) {
                JOptionPane.showMessageDialog(null, 
                    "Error: El botón debe estar en un JFrame",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFrame frame = (JFrame) window;

            JTextField txtNombre = buscarCampoPorNombreDeEtiqueta(frame, "Nombre:");
            JTextField txtAPaterno = buscarCampoPorNombreDeEtiqueta(frame, "Ap.Paterno:");
            JTextField txtAMaterno = buscarCampoPorNombreDeEtiqueta(frame, "Ap.Materno:");
            JTextField txtDireccion = buscarCampoPorNombreDeEtiqueta(frame, "Calle y numero:");
            JTextField txtColonia = buscarCampoPorNombreDeEtiqueta(frame, "Colonia:");
            JTextField txtTelefono = buscarCampoPorNombreDeEtiqueta(frame, "Telefono:");
            JTextField txtNoOrden = buscarCampoPorNombreDeEtiqueta(frame, "No. Orden:");
            JTextField txtFecha = buscarCampoPorNombreDeEtiqueta(frame, "Fecha Recibido:");
            JComboBox<String> cbTienda = buscarComboBox(frame);
            JTable tablaProductos = encontrarTabla(frame);
            JTextField lblTotal = buscarCampoPorNombreDeEtiqueta(frame, "Total:");
            
            if (txtNombre == null || txtDireccion == null || tablaProductos == null || lblTotal == null) {
                JOptionPane.showMessageDialog(null, 
                    "Faltan campos requeridos:\n" +
                    (txtNombre == null ? "- Nombre\n" : "") +
                    (txtDireccion == null ? "- Dirección\n" : "") +
                    (tablaProductos == null ? "- Tabla de productos\n" : "") +
                    (lblTotal == null ? "- Total\n" : ""),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String direccionCompleta = txtDireccion.getText() + 
                (txtColonia != null ? ", " + txtColonia.getText() : "");
            
            String nombreCompleto = txtNombre.getText() + 
                (txtAPaterno != null ? " " + txtAPaterno.getText() : "") + 
                (txtAMaterno != null ? " " + txtAMaterno.getText() : "");

            crearPDFCompleto(
                nombreCompleto,
                direccionCompleta,
                txtTelefono != null ? txtTelefono.getText() : "",
                txtNoOrden != null ? txtNoOrden.getText() : "",
                txtFecha != null ? txtFecha.getText() : "",
                cbTienda != null ? cbTienda.getSelectedItem().toString() : "",
                (DefaultTableModel) tablaProductos.getModel(),
                lblTotal.getText()
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Error inesperado:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private JTextField buscarCampoPorNombreDeEtiqueta(Container parent, String labelName) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel && ((JLabel)comp).getText().equals(labelName)) {
                Container parentContainer = comp.getParent();
                Component[] siblings = parentContainer.getComponents();
                for (int i = 0; i < siblings.length - 1; i++) {
                    if (siblings[i] == comp && siblings[i+1] instanceof JTextField) {
                        return (JTextField) siblings[i+1];
                    }
                }
            }
            if (comp instanceof Container) {
                JTextField found = buscarCampoPorNombreDeEtiqueta((Container) comp, labelName);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JComboBox<String> buscarComboBox(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JComboBox) {
                return (JComboBox<String>) comp;
            }
            if (comp instanceof Container) {
                JComboBox<String> found = buscarComboBox((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JTable encontrarTabla(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JTable) {
                return (JTable) comp;
            }
            if (comp instanceof Container) {
                JTable found = encontrarTabla((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JLabel findLabelByText(Container parent, String text) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel && ((JLabel)comp).getText().startsWith(text)) {
                return (JLabel) comp;
            }
            if (comp instanceof Container) {
                JLabel found = findLabelByText((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void crearPDFCompleto(String nombre, String direccion, String telefono, 
                                String noOrden, String fecha, String tienda,
                                DefaultTableModel modelo, String total) {
        try {
            String nombreArchivo = "Pedido_Pizza_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));
            doc.open();

            
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

            
            try {
                Image logo = Image.getInstance(getClass().getResource("/imagenes/logo_pizza.png"));
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                doc.add(logo);
            } catch (Exception e) {
                System.out.println("Logo no encontrado, se omite");
            }

            doc.add(new Paragraph("PIZZERÍA DELIZIOSA", fontTitulo));
            doc.add(new Paragraph("COMPROBANTE DE PEDIDO", fontSubtitulo));
            doc.add(Chunk.NEWLINE);

           
            doc.add(new Paragraph("DATOS DEL CLIENTE", fontSubtitulo));
            doc.add(new Paragraph("Nombre: " + nombre, fontNormal));
            doc.add(new Paragraph("Dirección: " + direccion, fontNormal));
            if (!telefono.isEmpty()) doc.add(new Paragraph("Teléfono: " + telefono, fontNormal));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("DETALLES DEL PEDIDO", fontSubtitulo));
            if (!noOrden.isEmpty()) doc.add(new Paragraph("Número de orden: " + noOrden, fontNormal));
            if (!fecha.isEmpty()) doc.add(new Paragraph("Fecha: " + fecha, fontNormal));
            if (!tienda.isEmpty()) doc.add(new Paragraph("Tienda: " + tienda, fontNormal));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("PRODUCTOS", fontSubtitulo));
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.addCell(new Phrase("Cantidad", fontSubtitulo));
            tabla.addCell(new Phrase("Producto", fontSubtitulo));
            tabla.addCell(new Phrase("Precio", fontSubtitulo));
            
            for (int i = 0; i < modelo.getRowCount(); i++) {
                tabla.addCell(new Phrase(modelo.getValueAt(i, 0).toString(), fontNormal));
                tabla.addCell(new Phrase(modelo.getValueAt(i, 1).toString(), fontNormal));
                tabla.addCell(new Phrase("$" + modelo.getValueAt(i, 2).toString(), fontNormal));
            }
            doc.add(tabla);
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("TOTAL: " + total, 
                  new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("¡Gracias por su pedido!", fontTitulo));

            doc.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(nombreArchivo));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al crear PDF: " + e.getMessage());
        }
    }
}
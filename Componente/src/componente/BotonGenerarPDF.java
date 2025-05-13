package componente;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BotonGenerarPDF extends JButton {

       
    private String tituloPDF = "Comprobante de Pago";
    private String nombreTienda = "Su tienda Favorita";
    private Color colorTitulo = Color.RED;
    private Color colorFondo = new Color(50, 150, 250); // Azul por defecto
    private Color colorTexto = Color.WHITE; // Blanco por defecto
    
    public BotonGenerarPDF() {
        configurarBoton();
        this.addActionListener(e -> generarPDF());
    }
    
    public String getTituloPDF() {
    return tituloPDF;
    }

    public void setTituloPDF(String tituloPDF) {
        this.tituloPDF = tituloPDF;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public Color getColorTitulo() {
        return colorTitulo;
    }

    public void setColorTitulo(Color colorTitulo) {
        this.colorTitulo = colorTitulo;
    }
        public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        this.setBackground(colorFondo); // Aplica el color inmediatamente
    }

    public Color getColorTexto() {
        return colorTexto;
    }

    public void setColorTexto(Color colorTexto) {
        this.colorTexto = colorTexto;
        this.setForeground(colorTexto); // Aplica el color inmediatamente
    }
    
    
   private void configurarBoton() {
        this.setText("Generar PDF");
        this.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        this.setBackground(colorFondo); // Usa la propiedad colorFondo
        this.setForeground(colorTexto); // Usa la propiedad colorTexto
        this.setFocusPainted(false);
    }

    private void generarPDF() {
        try {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            JTextField txtNoPedido = buscarTextField(frame, "txtNoPedido");
            JTextField txtNombre = buscarTextField(frame, "txtNombre");
            JTextField txtPaterno = buscarTextField(frame, "txtPaterno");
            JTextField txtMaterno = buscarTextField(frame, "txtMaterno");
            JTextField txtTelefono = buscarTextField(frame, "txtTelefono");
            JTextField txtDireccion = buscarTextField(frame, "txtDireccion");
            JTextField txtPrecio = buscarTextField(frame, "txtPrecio");
            
            JComboBox<String> cmbProducto = null;
            for (Component comp : frame.getContentPane().getComponents()) {
                if (comp instanceof JComboBox && comp.getName() != null && comp.getName().equals("cmbProducto")) {
                    cmbProducto = (JComboBox<String>) comp;
                    break;
                }
            }
            
            if (txtNombre == null || txtPaterno == null || cmbProducto == null || txtNoPedido == null) {
                JOptionPane.showMessageDialog(frame, "Faltan campos esenciales para generar el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Document document = new Document();
            String fileName = "Pedido_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        
            document.open();
          
            // CONVERTIR Color de Swing a BaseColor de iText
            BaseColor pdfColor = new BaseColor(
            colorTitulo.getRed(), 
            colorTitulo.getGreen(), 
            colorTitulo.getBlue()
            );
            
            
            
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 
                18, 
                com.itextpdf.text.Font.BOLD,
                pdfColor
            );

            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 
                12, 
                com.itextpdf.text.Font.NORMAL
            );

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 
                12, 
                com.itextpdf.text.Font.BOLD
            );
            // Título del PDF (usando propiedad)
            Paragraph titulo = new Paragraph(this.tituloPDF, titleFont); // Aquí el cambio
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            String nombreCompleto = txtNombre.getText() + " " + txtPaterno.getText() + 
                                   (txtMaterno != null ? " " + txtMaterno.getText() : "");
            
            Paragraph saludo = new Paragraph("Estimado(a) " + nombreCompleto + ":", boldFont);
            saludo.setSpacingAfter(10);
            document.add(saludo);

            String mensaje = "Muchas gracias por su compra de \"" + cmbProducto.getSelectedItem() + "\".\n\n" +
                            "Detalles de su pedido:\n" +
                            "- Número de pedido: " + txtNoPedido.getText() + "\n" +
                            (txtDireccion != null ? "- Dirección de envío: " + txtDireccion.getText() + "\n" : "") +
                            "- Producto: " + cmbProducto.getSelectedItem() + "\n" +
                            (txtPrecio != null ? "- Total a pagar: " + txtPrecio.getText() + "\n" : "") +
                            (txtTelefono != null ? "\nEn caso de cualquier duda, nos comunicaremos al: " + txtTelefono.getText() : "");

            Paragraph contenido = new Paragraph(mensaje, normalFont);
            contenido.setSpacingAfter(15);
            document.add(contenido);


            Paragraph despedida = new Paragraph(
            "\n¡Gracias por preferirnos!\n\n" +
            "Atentamente,\n" +
            this.nombreTienda + "\n" + // Aquí el cambio
            new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
            new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 
                10, 
                com.itextpdf.text.Font.ITALIC
            ));
            despedida.setAlignment(Element.ALIGN_CENTER);
            document.add(despedida);

            document.close();
            
            JOptionPane.showMessageDialog(frame, 
                "Comprobante generado exitosamente:\n" + fileName, 
                "PDF Generado", 
                JOptionPane.INFORMATION_MESSAGE);
            
            abrirArchivoPDF(fileName, frame);

        

                
            } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Error al generar PDF: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        }
    
     private void abrirArchivoPDF(String fileName, JFrame parentFrame) {
        try {
            if (Desktop.isDesktopSupported()) {
                File pdfFile = new File(fileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                        "El archivo PDF no se encontró",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                    "No se puede abrir el PDF automáticamente en este sistema",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentFrame,
                "Error al abrir el PDF: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

        private JTextField buscarTextField(Container container, String nombre) {
             for (Component comp : container.getComponents()) {
                if (comp instanceof JTextField && comp.getName() != null && comp.getName().equals(nombre)) {
                return (JTextField) comp;
            } else if (comp instanceof Container) {
                JTextField encontrado = buscarTextField((Container) comp, nombre);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }
}
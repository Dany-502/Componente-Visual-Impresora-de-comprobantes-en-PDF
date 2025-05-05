# Generador-de-comprobantes-en-formato-PDF

# Indice 
- [Autores](#Autores)
- [Explicación general](#Explicación-general)
- [Instrucciones de implementación ](#Instrucciones-de-implementación)
- [Instrucciones de uso](#Instrucciones-de-uso)
- [Explicación del código](#Explicación-del-código)
- [Video explicativo](#Video-explicativo)

# Autores
-- _Rodriguez Juarez Jose Daniel_

-- _Alonso Heredia Miguel Alberto_

# Explicación general
Componente visual que permite generar comprobantes de pago en formato PDF en base a los datos recaudados del usuario (Nombre completo, dirección, sucursal, nombre del local, productos, etc). El componente esta diseñado para ser utilizado en aplicaciones de registro de compras, proporcionando una forma sencilla de crear documentos PDF con los datos recaudados.


# Instrucciones de implementación 

## Implementar JFrame de prueba
1.-Descargar el archivo zip. Una vez descargado el .zip deberan descomprimirlo.

2.-Posteriormente deberemos buscarlo en la pc desde Netbeans, para eso seleccionamos el apartado "abrir proyecto".

3.-Busca el proyecto y abrelo.

4.-Una vez abierto les deberia de salir esto:

## Implementar componente a la paleta
1.- Crear y Limpiar el proyecto.

2.-Hacemos click derecho en la paleta y seleccionamos "palette manager".

3.-Una vez dentro de este apartado selecciona "nueva categoria".

4.-Te pedira darle un nombre a la categoria de tu paleta, asignale uno y dale ok.

5.-Regresamos a la ventana anterior y seleccionamos Add from JAR.

6.-En la carpeta del proyecto, para ser más precisos en la carpeta Dist debería aparecer el jar

7.-Abrimos y debemos seleccionar “Prueba”.

8.-Damos click en Next y elegimos en qué sección aparecerá el botón, nosotros creamos la sección llamada “Botón Comprobante”. Una vez seleccionado la sección finalizamos el proceso.

9.-El botón ya deberia aparecer en la paleta.



# Instrucciones de uso
1.-Selecciona el componente desde la paleta.

2.-Arrastralo a la posicion en donde quieres colocarlo dentro de tu JFrame

3.-El boton tiene implementado métodos que detectan objetos de tipo label, combobox , textField y table. Por lo tanto no te preocupes en agregar manualmente los datos 

4.-Ejecuta el codigo y revisa el resultado.

# Métodos de la clase

## Constructor

```java
public Prueba() {
  super("Generar Comprobante PDF");
  setCursor(new Cursor(Cursor.HAND_CURSOR));
  addActionListener(e -> generarPDF());
    }
```
El constructor de la clase Prueba configura el botón con el texto "Generar Comprobante PDF", le asigna un cursor de mano para mejorar la experiencia visual del usuario y le agrega un ActionListener. Este listener define la acción que debe ocurrir cuando el botón es presionado, en este caso, llamar al método generarPDF().

## generarPDF()

```java
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
```
Metodo que permite crear un archivo pdf con los datos recaudados del usuario. Se encarga de obtener la ventana(JFRAME) que contiene al botón, en caso de no encontrarlo, mostrara un mensaje de error. Posteriormente, utiliza utiliza varios métodos auxiliares para localizar campos específicos dentro de la interfaz gráfica (como nombre, dirección, productos, etc.). Valida que ciertos datos claves no estén vacios y ensambla todo los datos en un orden establecido. Finalmente llama al método crearPDFCompleto() para generar el documento PDF con toda la información recopilada.

## buscarCampoPorNombreDeEtiqueta(Container parent, String labelName)

```java
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
```
Este método recorre  todos los componentes gráficos dentro del contenedor padre para encontrar un "JTextField" que esté directamente asociado con un "JLabel" cuyo texto coincida con el argumento "labelName".

## buscarComboBox(Container parent)
```java
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
```
Este método busca y retorna objetos tipo "combobox" que encuentre dentro del JFRAME, desde los cuales se extraen los datos selecccionados dentro de ellos.

## encontrarTabla(Container parent)
```java
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
```
Este método busca y retorna un objeto de tipo JTable dentro del JFRAME, desde la cual se extraen los productos que el cliente ordenó.

## findLabelByText(Container parent, String text)
```java
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
```
Este método busca y retorna un objeto de tipo JLabel dentro del JFRAME, desde los cuales se extraen los datos introducidos dentro de ellos.

## crearPDFCompleto(String nombre, String direccion, String telefono, String noOrden, String fecha, String tienda, DefaultTableModel modelo, String total)
```java
private void crearPDFCompleto(String nombre, String direccion, String telefono, String noOrden, String fecha, String tienda,DefaultTableModel modelo, String total) {
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
```
Método encargado de construir el documento PDF utilizando la biblioteca "iText". Crea un archivo con nombre personalizado, añade logotipos, encabezados y datos del cliente, seguido de una tabla de productos con sus cantidades y precios. Luego muestra el total del pedido y un mensaje final. 

# Video explicativo
https://youtu.be/K6PsaPKJoDU?si=4Porj4kNKb9tt_M- 

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

# Instrucciones de uso

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

            
            JTextField txtNombre = findFieldByLabelName(frame, "Nombre:");
            JTextField txtAPaterno = findFieldByLabelName(frame, "Ap.Paterno:");
            JTextField txtAMaterno = findFieldByLabelName(frame, "Ap.Materno:");
            JTextField txtDireccion = findFieldByLabelName(frame, "Calle y numero:");
            JTextField txtColonia = findFieldByLabelName(frame, "Colonia:");
            JTextField txtTelefono = findFieldByLabelName(frame, "Telefono:");
            JTextField txtNoOrden = findFieldByLabelName(frame, "No. Orden:");
            JTextField txtFecha = findFieldByLabelName(frame, "Fecha Recibido:");
            JComboBox<String> cbTienda = findComboBox(frame);
            JTable tablaProductos = findTable(frame);
            JTextField lblTotal = findFieldByLabelName(frame, "Total:");
            

            
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
                lblTotal.getText() // Mostrará exactamente lo que diga el JLabel
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Error inesperado:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
```
Metodo que permite crear un archivo pdf con los datos recaudados del usuario. Se encarga de obtener la ventana(JFRAME) que contiene al botón, en caso de no encontrarlo, mostrara un mensaje de error. Posteriormente, utiliza utiliza varios métodos auxiliares para localizar campos específicos dentro de la interfaz gráfica (como nombre, dirección, productos, etc.). Valida que ciertos datos claves no estén vacios y ensambla todo los datos en un orden establecido. Finalmente llama al método crearPDFCompleto() para generar el documento PDF con toda la información recopilada.

## findFieldByLabelName(Container parent, String labelName)

```java
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
```

Este método recorre recursivamente todos los componentes gráficos dentro del contenedor padre para encontrar un JTextField que esté directamente asociado con un JLabel cuyo texto coincida con el argumento labelName.

# Video explicativo

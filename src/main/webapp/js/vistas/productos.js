
const API_URL = "ProductoJpaServlet";

export default {
    setup() {
        const { ref, onMounted } = Vue;
        // ==========================================
        // VARIABLES REACTIVAS
        // ==========================================
        const productos = ref([]);
        const cargando = ref(false);
        const guardando = ref(false);
        const form = ref(formularioInicial());
        // ==========================================
        // FORMULARIO INICIAL
        // ==========================================
        function formularioInicial() {
            return {
                id: null,
                nombre: "",
				proveedorId: null,
                tipoProducto: "",
                stockMinimo: null,
                stockMaximo: null
            };
        }
        // ==========================================
        // LISTAR PROVEEDORES
        // ==========================================
        async function listar() {
            cargando.value = true;
            try {
                const respuesta = await fetch(API_URL, {
                    method: "GET",
                    credentials: "same-origin",
                    headers: {
                        "Accept": "application/json"
                    }
                });
                if (!respuesta.ok) {
                    throw new Error("No se pudo consultar la lista.");
                }
                productos.value = await respuesta.json();
            } catch (error) {
                console.error("Error al listar productos:", error);
                alert("Error al consultar productos.");
            } finally {
                cargando.value = false;
            }
        }
        // ==========================================
        // GUARDAR PROVEEDOR
        // ==========================================
        async function guardar() {
            // Validar campos obligatorios
            if (
                !form.value.nombre.trim() ||
                !form.value.tipoProducto.trim() 
            ) {
                alert("Completa todos los campos.");
                return;
            }
            // Convertir códigos geográficos a números
            const proveedorId = Number(form.value.proveedorId);
            const stockMinimo = Number(form.value.stockMinimo);
            const stockMaximo = Number(form.value.stockMaximo);
            // Validar códigos geográficos
            if (
                !Number.isInteger(proveedorId) || proveedorId < 1 ||
                !Number.isInteger(stockMinimo) || stockMinimo < 1 ||
                !Number.isInteger(stockMaximo) || stockMaximo < 1
            ) {
                alert("Los códigos geográficos deben ser números enteros positivos.");
                return;
            }
            // Construir objeto JSON
            const datos = {
                nombre: form.value.nombre.trim(),
				proveedorId: proveedorId,
                tipoProducto: form.value.tipoProducto.trim(),
                stockMinimo: stockMinimo,
                stockMaximo: stockMaximo
            };
            // Si tiene ID, corresponde a una actualización
            if (form.value.id !== null && form.value.id !== "") {
                const id = Number(form.value.id);
                if (!Number.isInteger(id) || id < 1) {
                    alert("El código del producto no es válido.");
                    return;
                }
                datos.id = id;
            }
            guardando.value = true;
            try {
                const respuesta = await fetch(API_URL, {
                    method: "POST",
                    credentials: "same-origin",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    body: JSON.stringify(datos)
                });
                const resultado = await respuesta.json();
                if (!respuesta.ok) {
                    throw new Error(
                        resultado.error || "No se pudo guardar el producto."
                    );
                }
                alert(
                    resultado.mensaje ||
                    "Operación realizada correctamente."
                );
                limpiarFormulario();
                await listar();
            } catch (error) {
                console.error("Error al guardar producto:", error);
                alert(error.message);
            } finally {
                guardando.value = false;
            }
        }
        // ==========================================
        // EDITAR PROVEEDOR
        // ==========================================
        function editar(registro) {
            form.value = {
                id: registro.id,
                nombre: registro.nombre || "",
				proveedorId: Number(registro.proveedorId),
                tipoProducto: registro.tipoProducto || "",
                stockMinimo: Number(registro.stockMinimo),
                stockMaximo: Number(registro.stockMaximo)
            };
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        }
        // ==========================================
        // LIMPIAR FORMULARIO
        // ==========================================
        function limpiarFormulario() {
            form.value = formularioInicial();
        }
        // ==========================================
        // INICIALIZAR COMPONENTE
        // ==========================================
        onMounted(function() {
            listar();
        });
        // ==========================================
        // EXPONER VARIABLES Y FUNCIONES AL HTML
        // ==========================================
        return {
            productos,
            cargando,
            guardando,
            form,
            guardar,
            editar,
            limpiarFormulario
        };
    }
};

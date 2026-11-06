
const API_URL = "VendedorJpaServlet";

export default {

    setup() {

        const { ref, onMounted } = Vue;

        // ==========================================
        // VARIABLES REACTIVAS
        // ==========================================

        const vendedores = ref([]);
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
                tipoDocumento: "",
				tipoVendedor: "",
                numeroDocumento: "",
				telefono: "",
				porcentajeComision: null
            };

        }


        // ==========================================
        // LISTAR VendedorES
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
                vendedores.value = await respuesta.json();
            } catch (error) {
                console.error("Error al listar Vendedores:", error);
                alert("Error al consultar Vendedores.");
            } finally {
                cargando.value = false;
            }
        }


        // ==========================================
        // GUARDAR Vendedor
        // ==========================================

        async function guardar() {

            // Validar campos obligatorios
            if (
                !form.value.nombre.trim() ||
                !form.value.tipoVendedor.trim() ||
				!form.value.tipoDocumento.trim() ||
				!String(form.value.numeroDocumento).trim() ||
                !form.value.telefono.trim()
            ) {
                alert("Completa todos los campos.");
                return;
            }

            // Convertir Porcentaje de Comisión a números
            const porcentajeComision = Number(form.value.porcentajeComision);
			
            // Validar códigos geográficos
            if (
                isNaN(porcentajeComision) || porcentajeComision < 1 || porcentajeComision > 100
            ) {
                alert("El porcentaje de comisión debe ser numérico positivo entre 0.00 y 100.00 .");
                return;
            }

            // Construir objeto JSON
            const datos = {
                nombre: form.value.nombre.trim(),
				tipoVendedor: form.value.tipoVendedor.trim(),
                tipoDocumento: form.value.tipoDocumento.trim(),
                numeroDocumento: String(form.value.numeroDocumento).trim(),
				telefono: form.value.telefono.trim(),
                porcentajeComision: porcentajeComision
            };

            // Si tiene ID, corresponde a una actualización
            if (form.value.id !== null && form.value.id !== "") {
                const id = Number(form.value.id);
                if (!Number.isInteger(id) || id < 1) {
                    alert("El código del Vendedor no es válido.");
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
                        resultado.error || "No se pudo guardar el Vendedor."
                    );
                }
                alert(
                    resultado.mensaje ||
                    "Operación realizada correctamente."
                );
                limpiarFormulario();
                await listar();
            } catch (error) {
                console.error("Error al guardar Vendedor:", error);
                alert(error.message);
            } finally {
                guardando.value = false;
            }
        }

        // ==========================================
        // EDITAR Vendedor
        // ==========================================

        function editar(registro) {

            form.value = {
                id: registro.id,
                nombre: registro.nombre || "",
                tipoVendedor: registro.tipoVendedor || "",
                tipoDocumento: registro.tipoDocumento || "",
                numeroDocumento: registro.numeroDocumento || "",
				telefono: registro.telefono || "",
                porcentajeComision: Number(registro.porcentajeComision)
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
            vendedores,
            cargando,
            guardando,
            form,
            guardar,
            editar,
            limpiarFormulario
        };
    }
};

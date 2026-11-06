
const API_URL = "ProveedorJpaServlet";

export default {

    setup() {

        const { ref, onMounted } = Vue;

        // ==========================================
        // VARIABLES REACTIVAS
        // ==========================================

        const proveedores = ref([]);

        const cargando = ref(false);

        const guardando = ref(false);

        const form = ref(formularioInicial());


        // ==========================================
        // FORMULARIO INICIAL
        // ==========================================

        function formularioInicial() {

            return {
                id: null,
                ruc: "",
                razonSocial: "",
                telefono: "",
                direccion: "",
                distritoId: null,
                provinciaId: null,
                departamentoId: null
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

                proveedores.value = await respuesta.json();

            } catch (error) {

                console.error("Error al listar proveedores:", error);

                alert("Error al consultar proveedores.");

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
                !form.value.ruc.trim() ||
                !form.value.razonSocial.trim() ||
                !form.value.telefono.trim() ||
                !form.value.direccion.trim()
            ) {

                alert("Completa todos los campos.");

                return;
            }


            // Convertir códigos geográficos a números
            const distritoId = Number(form.value.distritoId);

            const provinciaId = Number(form.value.provinciaId);

            const departamentoId = Number(form.value.departamentoId);


            // Validar códigos geográficos
            if (
                !Number.isInteger(distritoId) || distritoId < 1 ||
                !Number.isInteger(provinciaId) || provinciaId < 1 ||
                !Number.isInteger(departamentoId) || departamentoId < 1
            ) {

                alert("Los códigos geográficos deben ser números enteros positivos.");

                return;
            }


            // Construir objeto JSON
            const datos = {

                ruc: form.value.ruc.trim(),

                razonSocial: form.value.razonSocial.trim(),

                telefono: form.value.telefono.trim(),

                direccion: form.value.direccion.trim(),

                distritoId: distritoId,

                provinciaId: provinciaId,

                departamentoId: departamentoId

            };


            // Si tiene ID, corresponde a una actualización
            if (form.value.id !== null && form.value.id !== "") {

                const id = Number(form.value.id);

                if (!Number.isInteger(id) || id < 1) {
                    alert("El código del proveedor no es válido.");
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
                        resultado.error || "No se pudo guardar el proveedor."
                    );

                }


                alert(
                    resultado.mensaje ||
                    "Operación realizada correctamente."
                );


                limpiarFormulario();

                await listar();


            } catch (error) {

                console.error("Error al guardar proveedor:", error);

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

                ruc: registro.ruc || "",

                razonSocial: registro.razonSocial || "",

                telefono: registro.telefono || "",

                direccion: registro.direccion || "",

                distritoId: Number(registro.distritoId),

                provinciaId: Number(registro.provinciaId),

                departamentoId: Number(registro.departamentoId)

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

            proveedores,

            cargando,

            guardando,

            form,

            guardar,

            editar,

            limpiarFormulario

        };

    }

};

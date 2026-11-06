const API_URL = "http://localhost:8081/api/stocks";

export default {

    setup() {

        const { ref, onMounted } = Vue;

        const stocks = ref([]);
        const cargando = ref(false);
        const guardando = ref(false);

        const formularioInicial = () => ({
            id: null,
            productoId: null,
            lote: "",
            fechaVencim: "",
            stockActual: 0
        });

        const form = ref(formularioInicial());


        // ==========================================
        // LISTAR
        // ==========================================

        async function listar() {

            cargando.value = true;

            try {

                const respuesta = await fetch(API_URL);

                if (!respuesta.ok) {
                    throw new Error("No se pudo consultar la lista de stocks.");
                }

                stocks.value = await respuesta.json();

            } catch (error) {

                console.error(error);
                alert("Error al consultar los stocks.");

            } finally {

                cargando.value = false;

            }

        }


        // ==========================================
        // GUARDAR / ACTUALIZAR
        // ==========================================

        async function guardar() {

            if (!form.value.productoId) {

                alert("Debe ingresar el ID del producto.");
                return;

            }

            if (form.value.stockActual < 0) {

                alert("El stock no puede ser negativo.");
                return;

            }

            guardando.value = true;

            try {

                const datos = {
                    productoId: Number(form.value.productoId),
                    lote: form.value.lote || null,
                    fechaVencim: form.value.fechaVencim || null,
                    stockActual: Number(form.value.stockActual)
                };

                let url = API_URL;
                let metodo = "POST";

                // Si existe ID, actualizamos
                if (form.value.id) {

                    url = API_URL + "/" + form.value.id;
                    metodo = "PUT";

                }

                const respuesta = await fetch(url, {

                    method: metodo,

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(datos)

                });

                if (!respuesta.ok) {

                    const texto = await respuesta.text();

                    throw new Error(
                        texto || "No se pudo guardar el stock."
                    );

                }

                alert(
                    form.value.id
                        ? "Stock actualizado correctamente."
                        : "Stock registrado correctamente."
                );

                limpiarFormulario();

                await listar();

            } catch (error) {

                console.error(error);
                alert(error.message);

            } finally {

                guardando.value = false;

            }

        }


        // ==========================================
        // EDITAR
        // ==========================================

        function editar(stock) {

            form.value = {
                id: stock.id,
                productoId: Number(stock.productoId),
                lote: stock.lote || "",
                fechaVencim: stock.fechaVencim || "",
                stockActual: Number(stock.stockActual)
            };

        }


        // ==========================================
        // ELIMINAR
        // ==========================================

        async function eliminar(id) {

            if (!confirm("¿Está seguro de eliminar este registro?")) {
                return;
            }

            try {

                const respuesta = await fetch(
                    API_URL + "/" + id,
                    {
                        method: "DELETE"
                    }
                );

                if (!respuesta.ok) {
                    throw new Error(
                        "No se pudo eliminar el registro."
                    );
                }

                alert("Stock eliminado correctamente.");

                await listar();

            } catch (error) {

                console.error(error);
                alert(error.message);

            }

        }


        // ==========================================
        // LIMPIAR FORMULARIO
        // ==========================================

        function limpiarFormulario() {

            form.value = formularioInicial();

        }


        // ==========================================
        // INICIO
        // ==========================================

        onMounted(listar);


        return {

            stocks,
            cargando,
            guardando,
            form,

            listar,
            guardar,
            editar,
            eliminar,
            limpiarFormulario

        };

    }

};
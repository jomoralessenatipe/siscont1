const API_URL = "http://localhost:8084/api/comprobantes";

export default {

    setup() {

        const { ref, computed } = Vue;

        const guardando = ref(false);

        const form = ref({
            clienteId: null,
            vendedorId: null,
            detalles: []
        });

        const detalle = ref({
            productoId: null,
            cantidad: 1,
            precioUnitario: 0
        });

        const totalNeto = computed(() => {

            return form.value.detalles.reduce(
                (total, item) =>
                    total +
                    (Number(item.cantidad) *
                     Number(item.precioUnitario)),
                0
            );

        });

        const igv = computed(() => {
            return totalNeto.value * 0.18;
        });

        const total = computed(() => {
            return totalNeto.value + igv.value;
        });

        function agregarDetalle() {

            if (
                !detalle.value.productoId ||
                Number(detalle.value.cantidad) <= 0 ||
                Number(detalle.value.precioUnitario) <= 0
            ) {

                alert("Complete correctamente los datos del producto.");
                return;
            }

            form.value.detalles.push({
                productoId: Number(detalle.value.productoId),
                cantidad: Number(detalle.value.cantidad),
                precioUnitario:
                    Number(detalle.value.precioUnitario)
            });

            detalle.value = {
                productoId: null,
                cantidad: 1,
                precioUnitario: 0
            };

        }

        function eliminarDetalle(index) {
            form.value.detalles.splice(index, 1);
        }

        async function guardar() {

            if (
                !form.value.clienteId ||
                !form.value.vendedorId
            ) {

                alert("Debe ingresar el cliente y vendedor.");
                return;
            }

            if (form.value.detalles.length === 0) {

                alert("Debe agregar al menos un producto.");
                return;
            }

            guardando.value = true;

            try {

                const respuesta = await fetch(API_URL, {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        clienteId:
                            Number(form.value.clienteId),

                        vendedorId:
                            Number(form.value.vendedorId),

                        detalles:
                            form.value.detalles
                    })

                });

                if (!respuesta.ok) {
                    throw new Error(
                        "No se pudo registrar el comprobante."
                    );
                }

                const resultado = await respuesta.json();

                alert(
                    "Comprobante registrado correctamente. ID: " +
                    resultado.id
                );

                form.value = {
                    clienteId: null,
                    vendedorId: null,
                    detalles: []
                };

            } catch (error) {

                console.error(error);
                alert(error.message);

            } finally {

                guardando.value = false;

            }

        }

        return {

            form,
            detalle,
            guardando,

            totalNeto,
            igv,
            total,

            agregarDetalle,
            eliminarDetalle,
            guardar

        };

    }

};
const { ref, onMounted } = Vue;

export default {
    setup() {
        // Objeto de Cabecera (Fecha y Glosa única para todo el asiento)
        const cabecera = ref({
            fecha: '',
            glosa: '',
            movimientos: []
        });
		
        // Objeto temporal para cada línea que se va agregando
        const nuevoMovimiento = ref({
            cuenta: '',
            debe: 0,
            haber: 0
        });

        const movimientos = ref([]);
        const cargando = ref(false);
        const guardando = ref(false);

        // Función para listar todos los movimientos contables en la tabla
        async function cargarMovimientos() {
            cargando.value = true;
            try {
                const res = await fetch('ContabilidadServlet');
                movimientos.value = await res.json();
            } catch (e) {
                console.error("Error al cargar contabilidad:", e);
            } finally {
                cargando.value = false;
            }
        }

        // Función para agregar una línea de cuenta a la lista temporal
        function agregarLineaMovimiento() {
            if (!nuevoMovimiento.value.cuenta) {
                alert("Por favor, ingrese la cuenta contable.");
                return;
            }

            // Normalizar valores numéricos de Debe y Haber
            let d = nuevoMovimiento.value.debe === '' || isNaN(nuevoMovimiento.value.debe) ? 0 : parseFloat(nuevoMovimiento.value.debe);
            let h = nuevoMovimiento.value.haber === '' || isNaN(nuevoMovimiento.value.haber) ? 0 : parseFloat(nuevoMovimiento.value.haber);

            if (d === 0 && h === 0) {
                alert("Debe ingresar un monto en el Debe o en el Haber.");
                return;
            }

            // Añadir copia de la línea a la lista de movimientos de la cabecera
            cabecera.value.movimientos.push({
                cuenta: nuevoMovimiento.value.cuenta,
                debe: d,
                haber: h
            });

            // Limpiar campos de la línea actual pero mantener la glosa y fecha
            nuevoMovimiento.value = { cuenta: '', debe: 0, haber: 0 };
        }

        // Función para quitar una línea de la tabla temporal
        function quitarLineaMovimiento(index) {
            cabecera.value.movimientos.splice(index, 1);
        }

        // Función para enviar todo el asiento completo (Cabecera + Detalles) al Servlet
        async function guardarAsientoCompleto() {
            if (!cabecera.value.fecha || !cabecera.value.glosa) {
                alert("Por favor, complete la fecha y la glosa general del asiento.");
                return;
            }

            if (cabecera.value.movimientos.length === 0) {
                alert("Debe agregar al menos una cuenta en los movimientos.");
                return;
            }

            console.log(">>> JS: Enviando asiento completo con cabecera y detalles:", cabecera.value);

            guardando.value = true;
            try {
                const res = await fetch('ContabilidadServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(cabecera.value)
                });
                
                console.log(">>> JS: Respuesta recibida del servidor, status:", res.status);

                if (!res.ok) {
                    throw new Error(`Servidor respondió con código ${res.status}`);
                }

                const data = await res.json().catch(() => ({ mensaje: "Asiento registrado con éxito" }));
                alert(data.mensaje || "Registrado correctamente");
                
                // Limpiar todo el formulario
                limpiarFormulario();
                await cargarMovimientos();
            } catch (e) {
                console.error(">>> JS ERROR: Error al conectar con el servidor:", e);
                alert("Error al registrar en el servidor (Revisa la consola de Java).");
            } finally {
                guardando.value = false;
            }
        }

        function limpiarFormulario() {
            cabecera.value = {
                fecha: '',
                glosa: '',
                movimientos: []
            };
            nuevoMovimiento.value = { cuenta: '', debe: 0, haber: 0 };
        }

        // Al iniciar el componente, carga los datos automáticamente
        onMounted(() => {
            cargarMovimientos();
        });

        return {
            cabecera,
            nuevoMovimiento,
            movimientos,
            cargando,
            guardando,
            cargarMovimientos,
            agregarLineaMovimiento,
            quitarLineaMovimiento,
            guardarAsientoCompleto,
            limpiarFormulario
        };
    }
};
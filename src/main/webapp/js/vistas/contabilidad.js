const { ref, onMounted } = Vue;

export default {
    setup() {
        // Variables reactivas siguiendo el estándar del proyecto
        const form = ref({ id: 0, cuenta: '', glosa: '', debe: '', haber: '' });
        const movimientos = ref([]);
        const cargando = ref(false);
        const guardando = ref(false);

        // Función para listar todos los movimientos contables
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

		// Función para guardar (Registrar movimiento) enviando JSON al Servlet
		async function guardar() {
		    // ---> Asegurar que los campos numéricos no vayan vacíos (evita el error 500)
		    form.value.debe = form.value.debe === '' || isNaN(form.value.debe) ? 0 : parseFloat(form.value.debe);
		    form.value.haber = form.value.haber === '' || isNaN(form.value.haber) ? 0 : parseFloat(form.value.haber);

		    // ---> Rastreo 1: Verificamos si entra a la función y qué datos lleva
		    console.log(">>> JS: Entrando a la función guardar(), datos a enviar:", form.value);

		    if (!form.value.cuenta || !form.value.glosa) {
		        alert("Por favor, complete la cuenta y la glosa.");
		        return;
		    }

		    guardando.value = true;
		    try {
		        // ---> Rastreo 2: Antes de disparar el fetch al Servlet
		        console.log(">>> JS: Disparando fetch hacia ContabilidadServlet...");

		        const res = await fetch('ContabilidadServlet', {
		            method: 'POST',
		            headers: { 'Content-Type': 'application/json' },
		            body: JSON.stringify(form.value)
		        });
		        
		        // ---> Rastreo 3: Verificando la respuesta del servidor
		        console.log(">>> JS: Respuesta recibida del servidor, status:", res.status);

		        // Si el servidor da error 500, capturamos el texto para ver qué falló exactamente
		        if (!res.ok) {
		            throw new Error(`Servidor respondió con código ${res.status}`);
		        }

		        const data = await res.json().catch(() => ({ mensaje: "Movimiento registrado con éxito" }));
		        alert(data.mensaje || "Registrado correctamente");
		        
		        limpiarFormulario();
		        await cargarMovimientos();
		    } catch (e) {
		        // ---> Rastreo si hay error en el fetch
		        console.error(">>> JS ERROR: Error al conectar con el servidor:", e);
		        alert("Error al registrar en el servidor (Revisa la consola de Java).");
		    } finally {
		        guardando.value = false;
		    }
		}

        function editar(registro) {
            form.value = { ...registro };
        }

        function limpiarFormulario() {
            form.value = { id: 0, cuenta: '', glosa: '', debe: '', haber: '' };
        }

        // Al iniciar el componente, carga los datos automáticamente
        onMounted(() => {
            cargarMovimientos();
        });

        return {
            form,
            movimientos,
            cargando,
            guardando,
            cargarMovimientos,
            guardar,
            editar,
            limpiarFormulario
        };
    }
};
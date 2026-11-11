const { ref, onMounted } = Vue;

export default {
    setup() {
        const form = ref({
            id: null,
            stockId: '',
            tipoMovimiento: 'ENTRADA',
            cantidad: '',
            comprobanteDetId: '',
            motivoMovimiento: ''
        });
        const movimientosKardex = ref([]);
        const cargando = ref(false);
        const guardando = ref(false);

        async function cargarKardexGeneral() {
            cargando.value = true;
            try {
                const res = await fetch('KardexJpaServlet');
                if (!res.ok) throw new Error("Error al conectar con el servidor");
                movimientosKardex.value = await res.json();
            } catch (e) {
                console.error("Error al cargar kardex:", e);
            } finally {
                cargando.value = false;
            }
        }

		async function guardar() {
		    // Construimos el objeto evitando mandar nulos en campos numéricos clave
		    const payload = {
		        stockId: parseInt(form.value.stockId) || 0,
		        tipoMovimiento: String(form.value.tipoMovimiento || 'ENTRADA'),
		        cantidad: parseInt(form.value.cantidad) || 0,
		        comprobanteDetId: parseInt(form.value.comprobanteDetId) || 0,
		        motivoMovimiento: String(form.value.motivoMovimiento || '')
		    };

		    // Solo agregamos el 'id' si realmente existe y es mayor a 0
		    if (form.value.id) {
		        payload.id = parseInt(form.value.id);
		    }

		    console.log(">>> KARDEX JS: Enviando datos al servlet:", payload);

		    if (!payload.stockId || !payload.cantidad || !payload.motivoMovimiento) {
		        alert("Por favor, complete los campos obligatorios.");
		        return;
		    }

		    guardando.value = true;
		    try {
		        const res = await fetch('KardexJpaServlet', {
		            method: 'POST',
		            headers: { 'Content-Type': 'application/json' },
		            body: JSON.stringify(payload)
		        });

		        console.log(">>> KARDEX JS: Código de respuesta del servidor:", res.status);

		        if (!res.ok) {
		            const errorText = await res.text();
		            console.error(">>> KARDEX JS ERROR TEXT:", errorText);
		            throw new Error(`Servidor respondió con código ${res.status}`);
		        }

		        const data = await res.json().catch(() => ({ mensaje: "Movimiento registrado con éxito" }));
		        alert(data.mensaje || "Registrado correctamente");

		        limpiarFormulario();
		        await cargarKardexGeneral();
		    } catch (e) {
		        console.error(">>> KARDEX JS EXCEPTION:", e);
		        alert("Error al registrar en el servidor (Revisa la consola de Eclipse).");
		    } finally {
		        guardando.value = false;
		    }
		}

        function limpiarFormulario() {
            form.value = {
                id: null,
                stockId: '',
                tipoMovimiento: 'ENTRADA',
                cantidad: '',
                comprobanteDetId: '',
                motivoMovimiento: ''
            };
        }

        onMounted(() => {
            cargarKardexGeneral();
        });

        return {
            form,
            movimientosKardex,
            cargando,
            guardando,
            cargarKardexGeneral,
            guardar,
            limpiarFormulario
        };
    }
};
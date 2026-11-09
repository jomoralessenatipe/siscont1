const { ref, onMounted } = Vue;

export default {
    setup() {
        // Variables reactivas usando el estándar del proyecto
        const form = ref({ id: 0, stockId: '', tipoMovimiento: 'ENTRADA', cantidad: '', comprobanteDetId: '', motivoMovimiento: '' });
        const movimientosKardex = ref([]);
        const stockIdFiltro = ref('');
        const cargando = ref(false);
        const guardando = ref(false);

        // Función para listar todo el inventario de tu Servlet
        async function cargarKardexGeneral() {
            cargando.value = true;
            try {
                const res = await fetch('KardexJpaServlet');
                movimientosKardex.value = await res.json();
            } catch (e) {
                console.error(e);
            } finally {
                cargando.value = false;
            }
        }

        // Función para filtrar por Lote/Stock
        async function filtrarPorStock() {
            if (!stockIdFiltro.value) return;
            cargando.value = true;
            try {
                const res = await fetch('KardexJpaServlet?stockId=' + stockIdFiltro.value);
                movimientosKardex.value = await res.json();
            } catch (e) {
                console.error(e);
            } finally {
                cargando.value = false;
            }
        }

        // Función para guardar (Insert / Update) enviando JSON al Servlet
        async function guardar() {
            guardando.value = true;
            try {
                const res = await fetch('KardexJpaServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(form.value)
                });
                const data = await res.json();
                alert(data.mensaje);
                limpiarFormulario();
                await cargarKardexGeneral();
            } catch (e) {
                console.error(e);
            } finally {
                guardando.value = false;
            }
        }

        function editar(registro) {
            form.value = { ...registro };
        }

        function limpiarFormulario() {
            form.value = { id: 0, stockId: '', tipoMovimiento: 'ENTRADA', cantidad: '', comprobanteDetId: '', motivoMovimiento: '' };
        }

        // Al iniciar el componente, carga los datos automáticamente
        onMounted(() => {
            cargarKardexGeneral();
        });

        return {
            form,
            movimientosKardex,
            stockIdFiltro,
            cargando,
            guardando,
            cargarKardexGeneral,
            filtrarPorStock,
            guardar,
            editar,
            limpiarFormulario
        };
    }
};

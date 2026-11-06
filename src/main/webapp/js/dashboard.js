
const { createApp, ref, nextTick, onMounted } = Vue;


// ==========================================
// CONFIGURACIÓN DE LOS MÓDULOS
// ==========================================

const modulos = {

    proveedores: {
        titulo: "Proveedores",
        vista: "./vistas/proveedores.html",
        componente: "./vistas/proveedores.js"
    },

    usuarios: {
        titulo: "Usuarios",
        vista: "./vistas/usuarios.html",
        componente: "./vistas/usuarios.js"
    },

    clientes: {
        titulo: "Clientes",
        vista: "./vistas/clientes.html",
        componente: "./vistas/clientes.js"
    },

    productos: {
        titulo: "Productos",
        vista: "./vistas/productos.html",
        componente: "./vistas/productos.js"
    },

    vendedores: {
        titulo: "Vendedores",
        vista: "./vistas/vendedores.html",
        componente: "./vistas/vendedores.js"
    },

    stocks: {
        titulo: "Stocks",
        vista: "./vistas/stocks.html",
        componente: "./vistas/stocks.js"
    },

    kardex: {
        titulo: "Kardex",
        vista: "./vistas/kardex.html",
        componente: "./vistas/kardex.js"
    },

    ventas: {
        titulo: "Registro de ventas",
        vista: "./vistas/ventas.html",
        componente: "./vistas/ventas.js"
    }

};


// ==========================================
// APLICACIÓN PRINCIPAL DEL DASHBOARD
// ==========================================

const dashboard = {

    setup() {

        // Variables reactivas
        const nombreUsuario = ref("");
        const moduloActual = ref("");
        const tituloModulo = ref("Bienvenido");
        const cargando = ref(false);
        const error = ref("");

        // Contenedor del módulo
        const panelModulo = ref(null);

        // Instancia de la aplicación Vue del módulo
        let aplicacionModulo = null;

        // Control de solicitudes simultáneas
        let solicitudActual = 0;


        // ==========================================
        // VERIFICAR SESIÓN
        // ==========================================

        async function verificarSesion() {

            try {

                const respuesta = await fetch("SesionJpaServlet", {
                    method: "GET",
                    credentials: "same-origin",
                    headers: {
                        "Accept": "application/json"
                    }
                });

                if (!respuesta.ok) {
                    window.location.href = "index.html";
                    return false;
                }

                const datos = await respuesta.json();

                nombreUsuario.value = datos.usuario;

                return true;

            } catch (e) {

                console.error("Error al verificar sesión:", e);

                window.location.href = "index.html";

                return false;
            }
        }


        // ==========================================
        // CARGAR MÓDULO
        // ==========================================

        async function cargarModulo(nombreModulo) {

            const modulo = modulos[nombreModulo];

            if (!modulo) {
                console.error("Módulo no encontrado:", nombreModulo);
                return;
            }

            const numeroSolicitud = ++solicitudActual;

            cargando.value = true;
            error.value = "";

            try {

                // Desmontar el módulo anterior
                if (aplicacionModulo !== null) {

                    aplicacionModulo.unmount();

                    aplicacionModulo = null;
                }

                // Descargar el HTML del módulo
                const respuesta = await fetch(modulo.vista, {
                    method: "GET",
                    credentials: "same-origin"
                });

                if (!respuesta.ok) {
                    throw new Error(
                        "No se pudo cargar la vista: " + modulo.vista
                    );
                }

                const html = await respuesta.text();

                // Comprobar que la solicitud siga vigente
                if (numeroSolicitud !== solicitudActual) {
                    return;
                }

                // Descargar el componente JavaScript
                const archivo = await import(modulo.componente);

                if (numeroSolicitud !== solicitudActual) {
                    return;
                }

                // Actualizar el título y el menú seleccionado
                tituloModulo.value = modulo.titulo;
                moduloActual.value = nombreModulo;

                // Ocultar indicador de carga
                cargando.value = false;

                // Esperar a que Vue actualice el DOM
                await nextTick();

                // Comprobar nuevamente la solicitud
                if (numeroSolicitud !== solicitudActual) {
                    return;
                }

                // Insertar la vista HTML
                panelModulo.value.innerHTML =
                    '<div id="vistaModulo">' + html + '</div>';

                // Obtener el contenedor del módulo
                const contenedor =
                    panelModulo.value.querySelector("#vistaModulo");

                // Crear y montar la aplicación Vue del módulo
                aplicacionModulo = createApp(archivo.default);

                aplicacionModulo.mount(contenedor);

            } catch (e) {

                if (numeroSolicitud === solicitudActual) {

                    console.error("Error al cargar módulo:", e);

                    error.value =
                        "No se pudo cargar el módulo seleccionado.";

                }

            } finally {

                if (numeroSolicitud === solicitudActual) {
                    cargando.value = false;
                }

            }
        }


        // ==========================================
        // CERRAR SESIÓN
        // ==========================================

        function cerrarSesion() {

            window.location.href = "LogoutJpaServlet";
        }


        // ==========================================
        // INICIAR DASHBOARD
        // ==========================================

        async function iniciarDashboard() {

            const sesionValida = await verificarSesion();

            if (sesionValida) {

                await cargarModulo("proveedores");

            }
        }


        // ==========================================
        // INICIALIZAR APLICACIÓN
        // ==========================================

        onMounted(function() {

            iniciarDashboard();

        });


        // ==========================================
        // DATOS Y FUNCIONES DISPONIBLES EN HTML
        // ==========================================

        return {

            nombreUsuario,
            moduloActual,
            tituloModulo,
            cargando,
            error,
            panelModulo,
            cargarModulo,
            cerrarSesion

        };

    }

};


// ==========================================
// MONTAR APLICACIÓN PRINCIPAL
// ==========================================

const aplicacionDashboard = createApp(dashboard);

aplicacionDashboard.mount("#app");

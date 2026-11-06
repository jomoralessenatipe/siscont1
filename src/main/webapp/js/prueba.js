const mensaje = "JavaScript moderno";

async function probar() {
    const respuesta = await fetch("SesionJpaServlet");
    console.log(respuesta);
}

probar();
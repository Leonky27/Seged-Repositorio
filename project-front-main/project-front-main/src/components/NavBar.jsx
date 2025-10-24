import React from 'react';
import { Link } from 'react-router-dom';

export function NavBar() {
  return (
    <>
      <nav className="navbar navbar-expand-lg pt-2 navbar-dark bg-dark">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/inicio">Seged</Link>
          <button 
            className="navbar-toggler" 
            type="button" 
            data-bs-toggle="collapse" 
            data-bs-target="#navbarNav" 
            aria-controls="navbarNav" 
            aria-expanded="false" 
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav">
              <li 
               className="nav-item">
                <Link to="/clientes" className="nav-link">Clientes</Link>
              </li>
              <li 
               className="nav-item">
                <Link to="/categorias" className="nav-link">Categorías</Link>
              </li>
              <li className="nav-item">
                <Link to="/proveedores" className="nav-link">Proveedores</Link>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="#">Ventas</a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="#">Inventario</a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="#">Dashboard</a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="#">Productos</a>
              </li>
           
            </ul>
          </div>
        </div>
      </nav>

    </>
  );
}


import { Routes, Route } from 'react-router-dom'
import { Login } from '../Main/Login'
import { ClienteRoute } from './ClienteRoute'
import RequireAuth from '../Auth/RequireAuth'
import { CategoriaRoute } from './CategoriaRoute'
import { ProveedorRoute } from './ProveedorRoute'
import { Register } from '../Main/Register'
import { DashboardRoute } from './DashboardRoute'
import { InventarioRoute } from './InventarioRoute'
import { ProductosRoute } from './ProductosRoute'
import { VentasRoute } from './VentasRoute'
import { InicioRoute } from './InicioRoute'

export const MainRoute = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="*" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/inicio" element={<RequireAuth><InicioRoute /></RequireAuth>} />
      <Route path="/categorias" element={<RequireAuth><CategoriaRoute /></RequireAuth>} />
      <Route path="/clientes" element={<RequireAuth><ClienteRoute /></RequireAuth>} />
      <Route path="/ventas" element={<RequireAuth><VentasRoute /></RequireAuth>} />
      <Route path="/productos" element={<RequireAuth><ProductosRoute /></RequireAuth>} />
      <Route path="/proveedores" element={<RequireAuth><ProveedorRoute /></RequireAuth>} />
      <Route path="/dashboard" element={<RequireAuth><DashboardRoute /></RequireAuth>} />
      <Route path="/inventario" element={<RequireAuth><InventarioRoute /></RequireAuth>} />



    </Routes>
  )
}

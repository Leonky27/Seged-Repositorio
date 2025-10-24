import { Routes, Route } from 'react-router-dom'
import { DashBoard } from '../Admin/DashBoard'
import { Ventas } from '../Admin/Ventas'
import { Productos } from '../Admin/Productos'
import { Login } from '../Main/Login'
import { Inicio } from '../Admin/Inicio'
import { ClienteRoute } from './ClienteRoute'
import RequireAuth from '../Auth/RequireAuth'
import { CategoriaRoute } from './CategoriaRoute'
import { ProveedorRoute } from './ProveedorRoute'

export const MainRoute = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="*" element={<Login />} />
      <Route path="/inicio" element={<RequireAuth><Inicio /></RequireAuth>} />
      <Route path="/categorias" element={<RequireAuth><CategoriaRoute /></RequireAuth>} />
      <Route path="/clientes" element={<RequireAuth><ClienteRoute /></RequireAuth>} />
      <Route path="/ventas" element={<RequireAuth><Ventas /></RequireAuth>} />
      <Route path="/productos" element={<RequireAuth><Productos /></RequireAuth>} />
      <Route path="/proveedores" element={<RequireAuth><ProveedorRoute /></RequireAuth>} />
      <Route path="/dashboard" element={<RequireAuth><DashBoard /></RequireAuth>} />

    </Routes>
  )
}

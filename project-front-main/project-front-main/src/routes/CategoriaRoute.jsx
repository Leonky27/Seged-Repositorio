import "bootstrap/dist/css/bootstrap.min.css";
import { NavBar } from "../components/NavBar";
import { Categoria } from "../Admin/Categoria";

export const CategoriaRoute = () => {
  return (
    <div
      className="d-flex flex-column"
      style={{ height: "100vh" }}
    >
      <div style={{ height: "17%" }}>
            <NavBar/>
      </div>
      <div style={{ height: "83%", overflow: "auto" }}>
        <Categoria/>
      </div>
    </div>
  );
};

package iaf.ofek.hadracha.base_course.web_server.EjectedPilotRescue;

import iaf.ofek.hadracha.base_course.web_server.Data.CrudDataBase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejectedPilotRescue")
public class EjectedPilotRescueController {
    private CrudDataBase crudDataBase;
    private AirplanesAllocationManager airplanesAllocationManager;

    public EjectedPilotRescueController(CrudDataBase dataBase, AirplanesAllocationManager airplanesAllocationManager) {
        this.crudDataBase = dataBase;
        this.airplanesAllocationManager = airplanesAllocationManager;
    }

    @GetMapping("/infos")
    public List<EjectedPilotInfo> getInfos(){
        return crudDataBase.getAllOfType(EjectedPilotInfo.class);
    }

    @GetMapping("/takeResponsibility")
    public void takeResponsibility(@RequestParam("ejectionId") int ejectionId, @CookieValue("client-id") String clientId){
        EjectedPilotInfo ejectedPilotInfo = crudDataBase.getByID(ejectionId, EjectedPilotInfo.class);
        if (ejectedPilotInfo == null) {
            throw new IllegalArgumentException("Error: No ejection found");
        }

        if (ejectedPilotInfo.getRescuedBy()==null) {
            ejectedPilotInfo.setRescuedBy(clientId);
            crudDataBase.update(ejectedPilotInfo);
            airplanesAllocationManager.allocateAirplanesForEjection(ejectedPilotInfo, clientId);
        }else
            return;

    }
}
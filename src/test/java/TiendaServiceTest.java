import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.entities.CafeEntity;

import co.edu.uniandes.dse.parcialejemplo.entities.TiendaEntity;
import co.edu.uniandes.dse.parcialejemplo.services.TiendaService;



import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;



public class TiendaServiceTest {




    @Autowired
	private TiendaService tiendaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<CafeEntity> cafeList = new ArrayList<>();
    private List<TiendaEntity> tiendaList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}


    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from CafeEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from TiendaEntity").executeUpdate();
	}


    private void insertData() {
		for (int i = 0; i < 3; i++) {
			CafeEntity authorEntity = factory.manufacturePojo(CafeEntity.class);
			entityManager.persist(authorEntity);
			cafeList.add(authorEntity);
		}
        for (int i = 0; i < 3; i++) {
			TiendaEntity tiendaEntity = factory.manufacturePojo(TiendaEntity.class);
			entityManager.persist(tiendaEntity);
			tiendaList.add(tiendaEntity);
		}
        for (int i = 0; i < 3; i++) {
			tiendaList.get(i).setCafes(cafeList);
		}

    }




	@Test
	void testCreateTienda() throws EntityNotFoundException, IllegalOperationException {
		TiendaEntity newEntity = factory.manufacturePojo(TiendaEntity.class);
		newEntity.setCafes(cafeList);
		TiendaEntity result = tiendaService.createTienda(newEntity);
		assertNotNull(result);
		TiendaEntity entity = entityManager.find(TiendaEntity.class, result.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getDireccion(), entity.getDireccion());
        assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getTelefono(), entity.getTelefono());


	}

	@Test
	void testCreateTiendaConTelefonoerroneo() {
		assertThrows(IllegalOperationException.class, () -> {
			TiendaEntity newEntity = factory.manufacturePojo(TiendaEntity.class);
			newEntity.setTelefono("626");
			newEntity.setNombre("nombre");
			newEntity.setDireccion("direccion");


			tiendaService.createTienda(newEntity);
		});
	}

}

package com.example.springjpahibernate;

import com.example.springjpahibernate.entities.Child;
import com.example.springjpahibernate.entities.Parent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.example.springjpahibernate.repositories.ChildRepository;
import com.example.springjpahibernate.repositories.ParentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class SpringframeworkApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private ChildRepository childRepository;

	@BeforeEach
	void setUp(){
		  parentRepository.deleteAll();
		  childRepository.deleteAll();
//          entityManager.createQuery("delete from Parent", Parent.class).executeUpdate();
//		  entityManager.createQuery("delete from Child", Child.class).executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE PARENT ALTER COLUMN ID RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE CHILD ALTER COLUMN ID RESTART WITH 1").executeUpdate();
	}

	//1. Save Parent without ID using repository.save(), entityManager.persist(), entityManager.merge(). Compare the results.
	@Test
	public void testSaveParentWithoutId(){
		Parent parent1 = new Parent();
		parent1.setName("Parent 1");
		Parent savedParent = parentRepository.save(parent1);
		assertNotNull(savedParent);
		assertEquals(1, parentRepository.findAll().size());
        parentRepository.deleteAll();
		assertEquals(0, parentRepository.findAll().size());
		System.out.println("Repository.save() generated ID: " + savedParent.getId());

		Parent parent2 = new Parent();
		parent2.setName("Parent 2");
		entityManager.persist(parent2);
		assertNotNull(parent2);
		TypedQuery<Parent> typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		List<Parent> parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		entityManager.remove(parent2);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.persist() generated ID: " + parent2.getId());

		Parent parent3 = new Parent();
		parent3.setName("Parent 3");
		savedParent = entityManager.merge(parent3);
		assertNotNull(parent3);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		entityManager.remove(savedParent);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.merge() generated ID: " + savedParent.getId());
	}

	// 2. Save Parent with an initialized ID using repository.save(), entityManager.persist(), entityManager.merge(). Compare the results.
	@Test
	public void testSaveParentWithInitializedId(){
		Parent parent1 = new Parent();
		parent1.setName("Parent 1");
		parent1.setId(1);
		Parent savedParent = parentRepository.save(parent1);
		assertNotNull(savedParent);
		assertEquals(1L, savedParent.getId());
		parentRepository.deleteAll();
		assertEquals(0, parentRepository.findAll().size());
		System.out.println("Repository.save() generated ID: " + savedParent.getId());

//		Parent parent2 = new Parent();
//		parent2.setName("Parent 2");
//		parent2.setId(2);
//		entityManager.persist(parent2);
//		Assertions.assertNotNull(parent2);
//		Assertions.assertEquals(2L,parent2.getId());
//		TypedQuery<Parent> typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
//		List<Parent> parentList = typedQuery.getResultList();
//		Assertions.assertEquals(1,parentList.size());
//		entityManager.remove(parent2);
//		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
//		parentList = typedQuery.getResultList();
//		Assertions.assertEquals(0,parentList.size());
//		System.out.println("entitymanager.persist() generated ID: " + parent2.getId());

		Parent parent3 = new Parent();
		parent3.setName("Parent 3");
		parent3.setId(3);
		savedParent = entityManager.merge(parent3);
		assertNotNull(parent3);
 		assertEquals(2L,savedParent.getId());
		TypedQuery<Parent> typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		List<Parent> parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		entityManager.remove(savedParent);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.merge() generated ID: " + savedParent.getId());
	}

	// 3.Insert Parent with some ID to the database. Save another Parent with the same ID using repository.save(), entityManager.persist(), entityManager.merge(). Compare the results.
	@Test
	public void testSaveParentWithDuplicateId(){
		Parent parent1 = new Parent();
		parent1.setName("Parent 1");
		Parent savedParent = parentRepository.save(parent1);
		assertNotNull(savedParent);
		System.out.println("1st Repository.save() generated ID: " + savedParent.getId());
		Parent parent11 = new Parent();
		parent11.setId(parent1.getId());
		parent11.setName("Parent 11");
		savedParent = parentRepository.save(parent11);
		assertNotNull(savedParent);
		System.out.println("2nd Repository.save() generated ID: " + savedParent.getId());
		parentRepository.deleteAll();
		assertEquals(0, parentRepository.findAll().size());

//		Parent parent2 = new Parent();
//		parent2.setName("Parent 2");
//		entityManager.persist(parent2);
//		Assertions.assertNotNull(parent2);
//		System.out.println("1st entitymanager.persist() generated ID: " + parent2.getId());
//		Parent parent21 = new Parent();
//		parent21.setName("Parent 21");
//		parent21.setId(parent2.getId());
//		entityManager.persist(parent21);
//		Assertions.assertNotNull(parent21);
//		entityManager.remove(parent2);
//		System.out.println("2nd entitymanager.persist() generated ID: " + parent2.getId());

		Parent parent3 = new Parent();
		parent3.setName("Parent 3");
		savedParent = entityManager.merge(parent3);
		assertNotNull(parent3);
		System.out.println("1st entitymanager.merge() generated ID: " + savedParent.getId());
		Parent parent31 = new Parent();
		parent31.setName("Parent 31");
		parent31.setId(savedParent.getId());
		savedParent = entityManager.merge(parent31);
		assertNotNull(parent3);
		entityManager.remove(savedParent);
		System.out.println("2nd entitymanager.merge() generated ID: " + savedParent.getId());
	}

	// 4. Save Parent with Children, which are not present in the database - using the same 3 approaches
	@Test
	public void testSaveParentWithChildren(){
		Parent parent1 = new Parent();
		parent1.setName("Parent 1");
		List<Child> childList1 = new ArrayList<>();
		Child child1 = new Child();
		child1.setName("Parent 1 Child 1");
		Child child2 = new Child();
		child2.setName("Parent 1 Child 2");
		childList1=List.of(child1,child2);
		parent1.setChildList(childList1);
		Parent savedParent = parentRepository.save(parent1);
		assertNotNull(savedParent);
		assertEquals(1, parentRepository.findAll().size());
		assertEquals(2,savedParent.getChildList().size());
		System.out.println(childRepository.findAll().size());
		parentRepository.deleteAll();
		childRepository.deleteAll();
		assertEquals(0, parentRepository.findAll().size());
		System.out.println("Repository.save() generated ID: " + savedParent.getId());

		Parent parent2 = new Parent();
		parent2.setName("Parent 2");
		List<Child> childList2 = new ArrayList<>();
		child1 = new Child();
		child1.setName("Parent 2 Child 1");
		child2 = new Child();
		child2.setName("Parent 2 Child 2");
		childList2=List.of(child1,child2);
		parent2.setChildList(childList2);
		entityManager.persist(parent2);
		assertNotNull(parent2);
		TypedQuery<Parent> typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		List<Parent> parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		System.out.println(childRepository.findAll().size());
		entityManager.remove(parent2);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.persist() generated ID: " + parent2.getId());

		Parent parent3 = new Parent();
		parent2.setName("Parent 3");
		List<Child> childList3 = new ArrayList<>();
		child1 = new Child();
		child1.setName("Parent 3 Child 1");
		child2 = new Child();
		child2.setName("Parent 3 Child 2");
		childList3=List.of(child1,child2);
		parent3.setChildList(childList3);
		savedParent = entityManager.merge(parent3);
		assertNotNull(parent3);
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		System.out.println(childRepository.findAll().size());
		entityManager.clear();
		typedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = typedQuery.getResultList();
		assertEquals(1,parentList.size());
		System.out.println("entitymanager.merge generated ID: " + savedParent.getId());
	}

	// 5. Save Parent with Children, which are already present in the database - using the same 3 approaches
	@Test
	public void testSaveParentWithChildAlreadyPresent(){
		Parent parent1 = new Parent();
		parent1.setName("Parent 1");
		List<Child> childList1 = new ArrayList<>();
		Child child1 = new Child();
		child1.setName("Parent 1 Child 1");
		Child child2 = new Child();
		child2.setName("Parent 1 Child 2");
		childRepository.save(child1);
		childRepository.save(child2);
		assertEquals(2, childRepository.findAll().size());
		childList1=List.of(child1,child2);
		parent1.setChildList(childList1);
		Parent savedParent = parentRepository.save(parent1);
		assertNotNull(savedParent);
		assertEquals(1, parentRepository.findAll().size());
		assertEquals(2,savedParent.getChildList().size());
		System.out.println(childRepository.findAll().size());
		parentRepository.deleteAll();
		childRepository.deleteAll();
		assertEquals(0, parentRepository.findAll().size());
		System.out.println("Repository.save() generated ID: " + savedParent.getId());

		Parent parent2 = new Parent();
		parent2.setName("Parent 2");
		List<Child> childList2 = new ArrayList<>();
		child1 = new Child();
		child1.setName("Parent 2 Child 1");
		child2 = new Child();
		child2.setName("Parent 2 Child 2");
		entityManager.persist(child1);
		entityManager.persist(child2);
		TypedQuery<Child> typedQuery = entityManager.createQuery("select c from Child c", Child.class);
		List<Child> childList = typedQuery.getResultList();
		assertEquals(2,childList.size());
		childList2=List.of(child1,child2);
		parent2.setChildList(childList2);
		entityManager.persist(parent2);
		assertNotNull(parent2);
		TypedQuery<Parent> TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		List<Parent> parentList = TypedQuery.getResultList();
		assertEquals(1,parentList.size());
		System.out.println(childRepository.findAll().size());
		entityManager.remove(parent2);
		entityManager.remove(child1);
		entityManager.remove(child2);
		TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = TypedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.persist() generated ID: " + parent2.getId());

		Parent parent3 = new Parent();
		parent2.setName("Parent 3");
		List<Child> childList3 = new ArrayList<>();
		child1 = new Child();
		child1.setName("Parent 3 Child 1");
		child2 = new Child();
		child2.setName("Parent 3 Child 2");
		entityManager.merge(child1);
		entityManager.merge(child2);
        typedQuery = entityManager.createQuery("select c from Child c", Child.class);
        childList = typedQuery.getResultList();
		assertEquals(2,childList.size());
		childList3=List.of(child1,child2);
		parent3.setChildList(childList3);
		savedParent = entityManager.merge(parent3);
		assertNotNull(parent3);
        TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = TypedQuery.getResultList();
		assertEquals(1,parentList.size());
		System.out.println(childRepository.findAll().size());
		entityManager.remove(savedParent);
		TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = TypedQuery.getResultList();
		assertEquals(0,parentList.size());
		System.out.println("entitymanager.merge generated ID: " + savedParent.getId());
	}

	// 6. Save Child without Parent - using the same 3 approaches
	@Test
	public void testSaveChildWithoutParent(){
		Child child = new Child();
		child.setName("Child 1");
		Child savedChild = childRepository.save(child);
		assertNotNull(savedChild);
		assertEquals(1, childRepository.findAll().size());
		childRepository.deleteAll();
		System.out.println("Repository,save() generated Id: " + savedChild.getId());

		Child child2 = new Child();
		child.setName("Child 2");
		entityManager.persist(child2);
		TypedQuery<Child> typedQuery = entityManager.createQuery("select c from Child c", Child.class);
		List<Child> childList = typedQuery.getResultList();
		assertEquals(1,childList.size());
		entityManager.remove(child2);
		System.out.println("entitymanager.persist() generated ID: " + child2.getId());

		Child child3 = new Child();
		child.setName("Child 3");
		savedChild = entityManager.merge(child3);
		typedQuery = entityManager.createQuery("select c from Child c", Child.class);
		childList = typedQuery.getResultList();
		assertEquals(1,childList.size());
		entityManager.remove(savedChild);
		System.out.println("entitymanager.persist() generated ID: " + savedChild.getId());
	}

	// 7. Save Child with Parent Initialized - using the same 3 approaches
	@Test
	public void testSaveChildWithParent(){
		Child child = new Child();
		child.setName("Child 1");
		Parent parent = new Parent();
		parent.setName("Parent 1");
		parent.getChildList().add(child);
		Child savedChild = childRepository.save(child);
		assertNotNull(savedChild);
		assertEquals(1, childRepository.findAll().size());
		System.out.println(parentRepository.findAll().size());
		childRepository.deleteAll();
		parentRepository.deleteAll();
		System.out.println("Repository.save() generated Id: " + savedChild.getId());

		Child child2 = new Child();
		child.setName("Child 2");
		parent = new Parent();
		parent.setName("Parent 1");
		parent.getChildList().add(child2);
		entityManager.persist(child2);
		TypedQuery<Child> typedQuery = entityManager.createQuery("select c from Child c", Child.class);
		List<Child> childList = typedQuery.getResultList();
		assertEquals(1,childList.size());
		TypedQuery<Parent> TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		List<Parent> parentList = TypedQuery.getResultList();
		System.out.println(parentList.size());
		entityManager.remove(child2);
		entityManager.remove(parent);
		System.out.println("entitymanager.persist() generated ID: " + child2.getId());

		child2 = new Child();
		child.setName("Child 2");
		parent = new Parent();
		parent.setName("Parent 1");
		parent.getChildList().add(child2);
		savedChild = entityManager.merge(child2);
		typedQuery = entityManager.createQuery("select c from Child c", Child.class);
		childList = typedQuery.getResultList();
		assertEquals(1,childList.size());
		TypedQuery = entityManager.createQuery("select p from Parent p", Parent.class);
		parentList = TypedQuery.getResultList();
		System.out.println(parentList.size());
		entityManager.remove(savedChild);
		entityManager.remove(parent);
		System.out.println("entitymanager.merge() generated ID: " + savedChild.getId());
	}

	// 8. Fetch the Parent with JpaRepository, try changing it and don’t save it explicitly. Flush the session and check whether the changes were propagated to the database
	@Test
	public void testFetchAndModifyParent() {
		Parent parent = Parent.builder().name("Parent").build();
		entityManager.persist(parent);
		entityManager.flush();
		entityManager.detach(parent);

		Parent fetchedParent = parentRepository.findById(parent.getId()).orElse(null);
		assertNotNull(fetchedParent);
		fetchedParent.setName("Modified Parent");

		entityManager.flush(); // Flushing changes
		Parent updatedParent = entityManager.find(Parent.class, fetchedParent.getId());
		assertEquals("Modified Parent", updatedParent.getName());
	}

	// 9. Start the transaction, fetch the Parent with JpaRepository, try changing it and don’t save it explicitly. Flush the session and check whether the changes were propagated to the database
	@Test
	@Transactional
	public void testTransactionFetchAndModifyParent() {
		Parent parent = Parent.builder().name("Parent").build();
		entityManager.persist(parent);
		entityManager.flush();
		entityManager.detach(parent);

		Parent fetchedParent = parentRepository.findById(parent.getId()).orElse(null);
		assertNotNull(fetchedParent);
		fetchedParent.setName("Modified Parent");

		// Need not to flush or commit transaction explicitly, Spring will handle it
		Parent updatedParent = parentRepository.findById(fetchedParent.getId()).orElse(null);
		assertEquals("Modified Parent", updatedParent.getName());
	}
}

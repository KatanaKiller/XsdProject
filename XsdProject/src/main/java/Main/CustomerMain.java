package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import trng.imcs.jaxb.Address;
import trng.imcs.jaxb.Customer;
import trng.imcs.jaxb.PaymentMethod;;

public class CustomerMain {
	static Customer cstm = new Customer();

	public static void main(String[] args) throws DatatypeConfigurationException, JAXBException {

		cstm.setCustomerId(11);
		cstm.setName("VIjay Dinanath Chauhan");
		cstm.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		cstm.setAnnualSalary(1000);

		Address address = new Address();
		address.setCity("Houston");
		address.setState("Texas");
		address.setZip(77705);
		List<Address> addList = cstm.getAddress();
		addList.add(address);

		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setCardName("VISA");
		paymentMethod.setCardNumber("1456289356477128");
		paymentMethod.setCardType("CreditCard");
		List<PaymentMethod> listPayment = cstm.getPayment();
		listPayment.add(paymentMethod);

		marshellingUnmarshell();
		
	}

	public static void marshellingUnmarshell() throws JAXBException {
		File file = new File("customerXML.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(cstm, file);
		jaxbMarshaller.marshal(cstm, System.out);
		
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Customer cstm2 = (Customer) unmarshaller.unmarshal(file);
		System.out.println(cstm2);
		
		serializeCustomer(cstm2);
		deserializeCustomer();
	}

	public static void deserializeCustomer() {
		try {
			byte[] data = Files.readAllBytes(Paths.get("gen.json"));
			ObjectMapper objMapper = new ObjectMapper();
			Customer cstm = objMapper.readValue(data, Customer.class);
			System.out.println(cstm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serializeCustomer(Customer cstm) {
		ObjectMapper objMapper = new ObjectMapper();

		File file = new File("newJson.json");
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);

			objMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objMapper.writeValue(fos, cstm);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

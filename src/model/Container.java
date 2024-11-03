package model;

public interface Container
{

	/**
	 * Get number of given resource in this container.
	 *
	 * @param resource the resource to proibe for
	 * @return the amount of resource in this container
	 */
	int getResourceNumber(Resource resource);

	/**
	 * IMPORTANT: You probably want to use ContainerManager.tranfer() instead.
	 * Remove resources from the container.
	 *
	 * @param resource the resource to remove
	 * @param quantity the quantity to remove
	 *                 <p>
	 *                 Shall throw an InventoryException if stock is insufficient
	 */
	void removeResource(Resource resource, int quantity);

	/**
	 * Get the remaining space for a given resource in this container.
	 * Remaining space may vary between resources if the storage limit is based on weight, for example.
	 *
	 * @param resource the resource to probe for
	 * @return the remaining space for this resource
	 */
	int getRemainingSpace(Resource resource);

	/**
	 * IMPORTANT: You probably want to use ContainerManager.tranfer() instead.
	 * Add resources to the container.
	 *
	 * @param resource the resource to add
	 * @param quantity the quantity to add
	 *                 <p>
	 *                 Shall throw an InventoryException if available space is insufficient
	 */
	void addResource(Resource resource, int quantity);

	/**
	 * Tranfer a resource to another container.
	 * @param destination target
	 * @param resource    resource to be transferred
	 * @param maxQuantity maximum quantity to tranfer
	 * @return the current number of items transferred
	 */
	default int tranferTo(Container destination, Resource resource, int maxQuantity)
	{
		int availableFromSource = this.getResourceNumber(resource);
		int availableSpaceInTarget = destination.getRemainingSpace(resource);
		int quantity = Math.min(Math.min(availableFromSource, availableSpaceInTarget), maxQuantity);

		this.removeResource(resource, quantity);
		destination.addResource(resource, quantity);

		// TODO: can't print one-line source or destination title, really ?
		System.out.println("Transferred " + quantity + " " + resource + ", destination remaining space: " + destination.getRemainingSpace(resource));

		return quantity;
	}

	class ContainerException extends RuntimeException
	{
		public ContainerException(String message)
		{
			super(message);
		}
	}

}

# MaintenanceRequestManagementApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**assign**](#assign) | **PATCH** /maintenance-requests/{id}/technician/{technicianId} | Assign a technician to a maintenance request|
|[**create4**](#create4) | **POST** /maintenance-requests | Create a new maintenance request|
|[**delete3**](#delete3) | **DELETE** /maintenance-requests/{id} | Delete a maintenance request|
|[**getOne3**](#getone3) | **GET** /maintenance-requests/{id} | Get a maintenance request by ID|
|[**listAll3**](#listall3) | **GET** /maintenance-requests | List all maintenance requests|
|[**update3**](#update3) | **PUT** /maintenance-requests/{id} | Update a maintenance request|

# **assign**
> MaintenanceRequestDto assign()

Assigns a technician to a maintenance request by their IDs.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

let id: string; //UUID of the maintenance request (default to undefined)
let technicianId: string; //UUID of the technician (default to undefined)

const { status, data } = await apiInstance.assign(
    id,
    technicianId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the maintenance request | defaults to undefined|
| **technicianId** | [**string**] | UUID of the technician | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Technician assigned |  -  |
|**404** | Maintenance request or technician not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create4**
> MaintenanceRequestDto create4(createMaintenanceRequestDto)

Creates a new maintenance request.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration,
    CreateMaintenanceRequestDto
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

let createMaintenanceRequestDto: CreateMaintenanceRequestDto; //

const { status, data } = await apiInstance.create4(
    createMaintenanceRequestDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createMaintenanceRequestDto** | **CreateMaintenanceRequestDto**|  | |


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Maintenance request created |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete3**
> DeleteResponseDto delete3()

Deletes a maintenance request by its ID.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

let id: string; //UUID of the maintenance request (default to undefined)

const { status, data } = await apiInstance.delete3(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**DeleteResponseDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request deleted |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getOne3**
> MaintenanceRequestDto getOne3()

Returns a maintenance request by its unique identifier.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

let id: string; //UUID of the maintenance request (default to undefined)

const { status, data } = await apiInstance.getOne3(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request found |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listAll3**
> Array<MaintenanceRequestDto> listAll3()

Returns a list of all maintenance requests.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

const { status, data } = await apiInstance.listAll3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<MaintenanceRequestDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of maintenance requests |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update3**
> MaintenanceRequestDto update3(updateRequestDto)

Updates an existing maintenance request.

### Example

```typescript
import {
    MaintenanceRequestManagementApi,
    Configuration,
    UpdateRequestDto
} from './api';

const configuration = new Configuration();
const apiInstance = new MaintenanceRequestManagementApi(configuration);

let id: string; //UUID of the maintenance request (default to undefined)
let updateRequestDto: UpdateRequestDto; //

const { status, data } = await apiInstance.update3(
    id,
    updateRequestDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateRequestDto** | **UpdateRequestDto**|  | |
| **id** | [**string**] | UUID of the maintenance request | defaults to undefined|


### Return type

**MaintenanceRequestDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Maintenance request updated |  -  |
|**404** | Maintenance request not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)


# TechnicianManagementApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**_delete**](#_delete) | **DELETE** /technicians/{id} | Delete a technician|
|[**create**](#create) | **POST** /technicians | Create a new technician|
|[**getOne**](#getone) | **GET** /technicians/{id} | Get a technician by ID|
|[**listAll**](#listall) | **GET** /technicians | List all technicians|
|[**update**](#update) | **PUT** /technicians/{id} | Update a technician|

# **_delete**
> DeleteResponseDto _delete()

Deletes a technician by their ID.

### Example

```typescript
import {
    TechnicianManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianManagementApi(configuration);

let id: string; //UUID of the technician (default to undefined)

const { status, data } = await apiInstance._delete(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the technician | defaults to undefined|


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
|**200** | Technician deleted |  -  |
|**404** | Technician not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create**
> TechnicianDto create(technicianCreateCommandDto)

Creates a new technician with the provided details.

### Example

```typescript
import {
    TechnicianManagementApi,
    Configuration,
    TechnicianCreateCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianManagementApi(configuration);

let technicianCreateCommandDto: TechnicianCreateCommandDto; //Technician creation data

const { status, data } = await apiInstance.create(
    technicianCreateCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **technicianCreateCommandDto** | **TechnicianCreateCommandDto**| Technician creation data | |


### Return type

**TechnicianDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Technician created |  -  |
|**400** | Invalid input data |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getOne**
> TechnicianDto getOne()

Returns a technician by their unique identifier.

### Example

```typescript
import {
    TechnicianManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianManagementApi(configuration);

let id: string; //UUID of the technician (default to undefined)

const { status, data } = await apiInstance.getOne(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the technician | defaults to undefined|


### Return type

**TechnicianDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Technician found |  -  |
|**404** | Technician not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listAll**
> Array<TechnicianDto> listAll()

Returns a list of all technicians.

### Example

```typescript
import {
    TechnicianManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianManagementApi(configuration);

const { status, data } = await apiInstance.listAll();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<TechnicianDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of technicians |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update**
> TechnicianDto update(technicianUpdateCommandDto)

Updates an existing technician by their ID.

### Example

```typescript
import {
    TechnicianManagementApi,
    Configuration,
    TechnicianUpdateCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new TechnicianManagementApi(configuration);

let id: string; //UUID of the technician (default to undefined)
let technicianUpdateCommandDto: TechnicianUpdateCommandDto; //Technician update data

const { status, data } = await apiInstance.update(
    id,
    technicianUpdateCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **technicianUpdateCommandDto** | **TechnicianUpdateCommandDto**| Technician update data | |
| **id** | [**string**] | UUID of the technician | defaults to undefined|


### Return type

**TechnicianDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Technician updated |  -  |
|**400** | Invalid input data |  -  |
|**404** | Technician not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

